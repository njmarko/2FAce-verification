<template>
  <div class="container" v-if="loaded">
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
import axios from "axios";
import * as faceapi from "face-api.js";
import { WebCam } from "vue-web-cam";
// const faceLandmarksDetection = require("@tensorflow-models/face-landmarks-detection");
// require("@tensorflow/tfjs-backend-webgl");

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
      net: null,
      loaded: false,
    };
  },
  async mounted() {
    const res = await axios.get("latest.weights", {
      responseType: "arraybuffer",
    });
    this.net = new faceapi.TinyYolov2();
    const weights = new Float32Array(res.data);
    this.net.load(weights);
    this.loaded = true;
    // console.log("LOADED WEIGHTS");
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
    async detectFace() {
      // const model = await faceLandmarksDetection.load(
      //   faceLandmarksDetection.SupportedPackages.mediapipeFacemesh
      // );
      // console.log(model);
      const imageFromElement = document.getElementById("img");
      var image = new Image();
      image.src = this.img;
      const predictions = await this.net.locateFaces(imageFromElement);
      // console.log(predictions);
      if (predictions.length > 0) {
        // console.log("DETECTED FACES: ", predictions.length);
        // Find the image which is the closest to the center
        const closestBoundBox = this.getClosestFaceBoundingBox(predictions);
        // Crop that image
        this.img = this.getFaceCropped(closestBoundBox, imageFromElement);
      } else {
        this.img = null;
      }
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
    getFaceCropped(closestBoundingBox, imageFromElement) {
      const width =
        closestBoundingBox.bottomRight.x - closestBoundingBox.topLeft.x;
      const height =
        closestBoundingBox.bottomRight.y - closestBoundingBox.topLeft.y;
      return this.cropImage(
        imageFromElement,
        closestBoundingBox.topLeft.x,
        closestBoundingBox.topLeft.y,
        width,
        height
      ).toDataURL("image/jpg", 90);
    },
    getClosestFaceBoundingBox(predictions) {
      const centerPoint = {
        x: this.$refs.webcam.$el.videoWidth / 2,
        y: this.$refs.webcam.$el.videoHeight / 2,
      };
      let closestPoint = { x: 99999999, y: 99999999 };
      let closestBoundingBox = {};
      predictions.forEach((prediction) => {
        const boundingBox = prediction.box;
        const point = this.getBoundingBoxCenterPoint(boundingBox);
        if (this.isCloserToCenter(centerPoint, point, closestPoint)) {
          closestPoint = point;
          closestBoundingBox = boundingBox;
        }
      });
      return closestBoundingBox;
    },
    isCloserToCenter(centerPoint, point, closestPoint) {
      return (
        this.euclideanDistance(centerPoint, point) <
        this.euclideanDistance(centerPoint, closestPoint)
      );
    },
    getBoundingBoxCenterPoint(boundingBox) {
      return {
        x: (boundingBox.bottomRight.x + boundingBox.topLeft.x) / 2,
        y: (boundingBox.bottomRight.y + boundingBox.topLeft.y) / 2,
      };
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