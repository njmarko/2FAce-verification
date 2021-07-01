<template>
  <div class="container">
    <div class="row">
      <div class="col-md-6">
        <h2>Current Camera</h2>
        <code v-if="device">{{ device.label }}</code>
        <div class="border">
          <vue-web-cam
            ref="webcam"
            :device-id="deviceId"
            width="100%"
            @started="onStarted"
            @stopped="onStopped"
            @error="onError"
            @cameras="onCameras"
            @camera-change="onCameraChange"
          />
        </div>

        <div class="row">
          <div class="col-md-12">
            <select v-model="camera">
              <option>-- Select Device --</option>
              <option
                v-for="device in devices"
                :key="device.deviceId"
                :value="device.deviceId"
              >
                {{ device.label }}
              </option>
            </select>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <h2>Captured Image</h2>
        <figure class="figure">
          <img id="img" :src="img" class="img-responsive" alt="Cropped image" />
        </figure>
      </div>
    </div>
  </div>
</template>

<script>
import { WebCam } from "vue-web-cam";
const faceLandmarksDetection = require("@tensorflow-models/face-landmarks-detection");
require("@tensorflow/tfjs-backend-webgl");

export default {
  name: "App",
  components: {
    "vue-web-cam": WebCam,
  },
  data() {
    return {
      img: null,
      camera: null,
      deviceId: null,
      devices: [],
    };
  },
  computed: {
    device: function () {
      return this.devices.find((n) => n.deviceId === this.deviceId);
    },
  },
  watch: {
    camera: function (id) {
      this.deviceId = id;
    },
    devices: function () {
      if (this.devices.length >= 1) {
        this.camera = this.devices[0].deviceId;
        this.deviceId = this.devices[0].deviceId;
      }
    },
  },
  methods: {
    async onStarted(stream) {
      console.log("On Started Event", stream);
    },
    onStopped(stream) {
      console.log("On Stopped Event", stream);
    },
    onStop() {
      this.$refs.webcam.stop();
    },
    async onStart() {
      this.$refs.webcam.start();
    },
    onError(error) {
      console.log("On Error Event", error);
    },
    onCameras(cameras) {
      this.devices = cameras;
      console.log("On Cameras Event", cameras);
    },
    onCameraChange(deviceId) {
      this.deviceId = deviceId;
      this.camera = deviceId;
      console.log("On Camera Change Event", deviceId);
    },
    async getBase64Face() {
      this.img = this.$refs.webcam.capture();
      await this.detectFace();
      return this.img;
    },
    cropImage(src, sx, sy, width, height) {
      // create empty canvas
      var canvas = document.createElement("canvas");
      canvas.width = width;
      canvas.height = height;
      canvas
        .getContext("2d")
        .drawImage(src, sx, sy, width, height, 0, 0, width, height);
      return canvas;
    },
    async detectFace() {
      const model = await faceLandmarksDetection.load(
        faceLandmarksDetection.SupportedPackages.mediapipeFacemesh
      );

      this.image_from_element = document.getElementById("img");
      var image = new Image();
      image.src = this.img;

      const predictions = await model.estimateFaces({
        input: this.image_from_element,
      });
      if (predictions.length > 0) {
        console.log("DETECTED FACES: ", predictions.length);
        // Find the image which is the closest to the center
        const centerPoint = {
          x: this.$refs.webcam.$el.videoWidth / 2,
          y: this.$refs.webcam.$el.videoHeight / 2,
        };
        let closestPoint = { x: 99999999, y: 99999999 };
        let closestBoundingBox = {};
        predictions.forEach((prediction) => {
          const boundingBox = prediction.boundingBox;
          const point = {
            x: (boundingBox.bottomRight[0] + boundingBox.topLeft[0]) / 2,
            y: (boundingBox.bottomRight[1] + boundingBox.topLeft[1]) / 2,
          };
          if (
            this.euclideanDistance(centerPoint, point) <
            this.euclideanDistance(centerPoint, closestPoint)
          ) {
            closestPoint = point;
            closestBoundingBox = boundingBox;
          }
        });
        // Crop that image
        const width =
          closestBoundingBox.bottomRight[0] - closestBoundingBox.topLeft[0];
        const height =
          closestBoundingBox.bottomRight[1] - closestBoundingBox.topLeft[1];
        this.img = this.cropImage(
          this.image_from_element,
          closestBoundingBox.topLeft[0],
          closestBoundingBox.topLeft[1],
          width,
          height
        ).toDataURL("image/jpg", 90);
      } else {
        this.img = null;
      }
    },
    euclideanDistance(point1, point2) {
      return Math.sqrt(
        Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2)
      );
    },
  },
};
</script>
<style scoped>
</style>