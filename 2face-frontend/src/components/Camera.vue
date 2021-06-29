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
                            >{{ device.label }}</option>
                        </select>
                    </div>
                    <div class="col-md-12">
                        <button type="button" class="btn btn-primary" @click="onCapture">Capture Photo</button>
                        <button type="button" class="btn btn-danger" @click="onStop">Stop Camera</button>
                        <button type="button" class="btn btn-success" @click="onStart">Start Camera</button>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h2>Captured Image</h2>
                <figure class="figure">
                    <img id="img" :src="img" class="img-responsive" />
                </figure>
            </div>
        </div>
    </div>
</template>

<script>
import {WebCam} from "vue-web-cam";
const faceLandmarksDetection = require('@tensorflow-models/face-landmarks-detection');
require('@tensorflow/tfjs-backend-webgl');

export default {
    name: "App",
    components: {
        "vue-web-cam": WebCam
    },
    data() {
        return {
            img: null,
            camera: null,
            deviceId: null,
            devices: []
        };
    },
    computed: {
        device: function() {
            return this.devices.find(n => n.deviceId === this.deviceId);
        }
    },
    watch: {
        camera: function(id) {
            this.deviceId = id;
        },
        devices: function() {
            // Once we have a list select the first one
            const [first, ...tail] = this.devices;
			tail;
            if (first) {
                this.camera = first.deviceId;
                this.deviceId = first.deviceId;
            }
        }
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
        cropImage( src, sx, sy, width, height ){
        // create empty canvas
        var canvas = document.createElement("canvas");                  
        canvas.width  = width;
        canvas.height = height;
        canvas.getContext("2d").drawImage(src, sx, sy, width, height, 0, 0, width, height);
        return canvas;
    },
        async detectFace() {
         // Load the MediaPipe Facemesh package.
         const model = await faceLandmarksDetection.load(
           faceLandmarksDetection.SupportedPackages.mediapipeFacemesh);

         // Pass in a video stream (or an image, canvas, or 3D tensor) to obtain an
         // array of detected faces from the MediaPipe graph. If passing in a video
         // stream, a single prediction pethis.imgr frame will be returned.

        this.img = this.$refs.webcam.capture();
        this.image_from_element = document.getElementById("img");
        var image = new Image();
        image.src = this.img;

         const predictions = await model.estimateFaces({
        //    input:this.$refs.webcam.$el 
            input:this.image_from_element
         });

        if (predictions.length > 0) {
           /*
           `predictions` is an array of objects describing each detected face, for example:

           [
             {
               faceInViewConfidence: 1, // The probability of a face being present.
               boundingBox: { // The bounding box surrounding the face.
                 topLeft: [232.28, 145.26],
                 bottomRight: [449.75, 308.36],
               },
               mesh: [ // The 3D coordinates of each facial landmark.
                 [92.07, 119.49, -17.54],
                 [91.97, 102.52, -30.54],
                 ...
               ],
               scaledMesh: [ // The 3D coordinates of each facial landmark, normalized.
                 [322.32, 297.58, -17.54],
                 [322.18, 263.95, -30.54]
               ],
               annotations: { // Semantic groupings of the `scaledMesh` coordinates.
                 silhouette: [
                   [326.19, 124.72, -3.82],
                   [351.06, 126.30, -3.00],
                   ...
                 ],
                 ...
               }
             }
           ]
           */

            for (let i = 0; i < predictions.length; i++) {
              const keypoints = predictions[i].boundingBox;
                var width = keypoints.bottomRight[0] - keypoints.topLeft[0];
                var height = keypoints.bottomRight[1] - keypoints.topLeft[1];
                this.img =this.cropImage(image, keypoints.topLeft[0], keypoints.topLeft[1], width, height).toDataURL('image/jpg', 90);
            }

            }
        }

    }
};

</script>
<style scoped>

</style>