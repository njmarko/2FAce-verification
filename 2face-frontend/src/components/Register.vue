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
              {{ imageCount }}</label
            >
            <div class="progress">
              <div
                class="progress-bar progress-bar-blocks progress-bar-animated"
                role="progressbar"
                v-bind:style="progressBarStyle"
              ></div>
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
        </form>
      </div>
    </div>
    <camera ref="cam" />
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
      errorMessage: "",
      imageCount: 25,
    };
  },
  methods: {
    async register() {
      if (this.payload.confirmPassword != this.payload.password) {
        return;
      }

      try {
        this.isRegistering = true;
        for (let index = 0; index < this.imageCount; index++) {
          this.payload.images.push(await this.$refs.cam.getBase64Face());
        }
        const response = await userService.register(this.payload);
        alert(
          `${response.data.username}, you have been registered successfully!`
        );
        this.$router.push({ name: "Login" });
      } catch (error) {
        if (error.response) {
          this.errorMessage = error.response.data.message;
          this.$refs.errorModalRef.showModal();
        }
      }
      this.payload.images = [];
      this.isRegistering = false;
    },
  },
  computed: {
    progressBarStyle: function () {
      return `width: ${(100 * this.payload.images.length) / this.imageCount}%;`;
    },
  },
};
</script>
