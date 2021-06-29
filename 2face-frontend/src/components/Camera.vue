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
          <div class="col-md-12">
            <button type="button" class="btn btn-primary" @click="onCapture">
              Capture Photo
            </button>
            <button type="button" class="btn btn-danger" @click="onStop">
              Stop Camera
            </button>
            <button type="button" class="btn btn-success" @click="onStart">
              Start Camera
            </button>
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
      // Once we have a list select the first one
      const [first, ...tail] = this.devices;
      tail;
      if (first) {
        this.camera = first.deviceId;
        this.deviceId = first.deviceId;
      }
    },
  },
  methods: {
    async onCapture() {
      this.img = this.$refs.webcam.capture();
      await this.detectFace();
    },
    async onStarted(stream) {
      console.log("On Started Event", stream);
    },
    onStopped(stream) {
      console.log("On Stopped Event", stream);
    },
    onStop() {
      this.$refs.webcam.stop();
    },
    onStart() {
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
        //    input:this.$refs.webcam.$el
        input: this.image_from_element,
      });

      if (predictions.length > 0) {
        for (const prediction of predictions) {
          const keypoints = prediction.boundingBox;
          const width = keypoints.bottomRight[0] - keypoints.topLeft[0];
          const height = keypoints.bottomRight[1] - keypoints.topLeft[1];
          this.img = this.cropImage(
            this.image_from_element,
            keypoints.topLeft[0],
            keypoints.topLeft[1],
            width,
            height
          ).toDataURL("image/jpg", 90);
        }
      }
    },
  },
};
</script>
<style scoped>
</style>