<template>
  <div class="container">
    <div class="card mb-4">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Register</h4>
      </div>
      <div class="card-body">
        <form @submit.prevent="register">
          <div class="form-group row">
            <label for="inputUsername" class="col-sm-2 col-form-label"
              ><img
                alt="Username icon"
                src="assets/icons/user_world-1.png"
                class="icon-16-4"
              />Username</label
            >
            <div class="col-sm-10">
              <input
                type="text"
                class="form-95"
                id="inputUsername"
                required
                v-model="payload.username"
              />
            </div>
          </div>
          <div class="form-group row">
            <label for="inputEmail" class="col-sm-2 col-form-label"
              ><img
                alt="Email icon"
                src="assets/icons/envelope_closed-1.png"
                class="icon-16-4"
              />Email</label
            >
            <div class="col-sm-10">
              <input
                type="email"
                class="form-95"
                id="inputEmail"
                required
                v-model="payload.email"
              />
            </div>
          </div>
          <div class="form-group row">
            <label for="inputPassword3" class="col-sm-2 col-form-label"
              ><img
                alt="Password icon"
                src="assets/icons/keys-0.png"
                class="icon-16-4"
              />
              Password</label
            >
            <div class="col-sm-10">
              <input
                type="password"
                class="form-95"
                id="inputPassword3"
                required
                v-model="payload.password"
              />
            </div>
          </div>
          <div class="form-group row">
            <label for="inputPassword4" class="col-sm-2 col-form-label"
              ><img
                alt="Confirm password icon"
                src="assets/icons/keys-0.png"
                class="icon-16-4"
              />
              Confirm password</label
            >
            <div class="col-sm-10">
              <input
                type="password"
                class="form-95"
                id="inputPassword4"
                required
                v-model="payload.confirmPassword"
              />
            </div>
          </div>
          <div class="form-group" v-if="isRegistering">
            <label
              >Collecting images: {{ payload.images.length }} /
              {{ imageCount }} (Make sure your face is the only one visible to
              the camera)</label
            >
            <div class="progress">
              <div
                class="progress-bar progress-bar-blocks progress-bar-animated"
                role="progressbar"
                v-bind:style="progressBarStyle"
              ></div>
            </div>
          </div>
          <div class="form-group" v-if="isTraining">
            <label
              >Your face verification model is training. Please be
              patient.</label
            >
            <div class="text-center">
              <div class="spinner-border" role="status">
                <span class="sr-only">Loading...</span>
              </div>
            </div>
          </div>
          <div class="form-group row">
            <div class="col-sm-12">
              <button
                type="submit"
                class="btn btn-primary"
                v-b-modal.errMsgModal
              >
                Register
              </button>
            </div>
          </div>
          <div style="text-align: center">
            <p>
              Already have an account?
              <router-link :to="{ name: 'Login' }">Login here!</router-link>
            </p>
          </div>
        </form>
      </div>
    </div>
    <div class="card mb-4">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Cameras</h4>
      </div>
      <div class="card-body">
        <camera ref="cam" />
      </div>
    </div>


    <error-message-modal
      ref="errorModalRef"
      id="errorModal"
      :errorMessage="errorMessage"
    />
  </div>
</template>


<script>
import ErrorMessageModal from "@/components/modals/ErrorMessageModal";
import Camera from "../components/Camera.vue";

import userService from "@/service/UserService";

export default {
  components: {
    ErrorMessageModal,
    Camera,
  },
  data() {
    return {
      payload: {
        username: "",
        password: "",
        email: "",
        confirmPassword: "",
        images: [],
      },
      isRegistering: false,
      isTraining: false,
      errorMessage: "",
      imageCount: 25,
    };
  },
  mounted(){

    if(this.$agent){
        this.$agent.stopCurrent();
        this.$agent.speak("Hello sir, welcome to registration page.");
        this.$agent.play('GetAttention');
        this.$agent.speak("Please fill you registration data and click register button.");
        this.$agent.play('Searching');
    }

  },
  methods: {
    async register() {
      if (this.payload.confirmPassword != this.payload.password) {
        return;
      }

      try {
        this.$agent.stopCurrent();
        this.$agent.speak("Sir, smile for the camera while it takes pictures");
        this.$agent.play('GestureUp');
        this.isRegistering = true;
        while (this.payload.images.length < this.imageCount) {
          const takenImage = await this.$refs.cam.getBase64Face();
          if (takenImage) {
            this.payload.images.push(takenImage);
          } else {
            this.showErrorModal(
              "Unable to detect single face on the camera. Make sure your face is the one visible to the camera."
            );
          }
        }
        this.isRegistering = false;
        this.isTraining = true;
        this.$agent.stopCurrent();
        this.$agent.speak("Please be patient my friend for the registration to finish.");
        this.$agent.play('Searching');
        this.$agent.play('Searching');
        this.$agent.play('Searching');
        const response = await userService.register(this.payload);
        this.$agent.stopCurrent();
        this.$agent.speak(`Congratulations my friend, ${response.data.username}. You have registered successfully!`);
        this.$agent.play('Congratulate');
        this.$router.push({ name: "Login" });
      } catch (error) {
        if (error.response) {
          this.$agent.stopCurrent();
          this.$agent.speak("Sorry my friend. Registration was unsuccessfull. Try again please.");
          this.$agent.play('Sad');
          this.showErrorModal(error.response.data.message);
        }
      }
      this.isTraining = false;
      this.payload.images = [];
    },
    showErrorModal(message) {
      this.errorMessage = message;
      this.$refs.errorModalRef.showModal();


    },
  },
  computed: {
    progressBarStyle: function () {
      return `width: ${(100 * this.payload.images.length) / this.imageCount}%;`;
    },
  },
};
</script>
