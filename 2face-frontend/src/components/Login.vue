<template>
  <div class="container">
    <div class="card mb-4">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Sign in</h4>
      </div>
      <div class="card-body">
        <form @submit.prevent="login">
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
                v-model="payload.username"
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
                v-model="payload.password"
              />
            </div>
          </div>
          <div class="form-group row">
            <div class="col-sm-12">
              <button type="submit" class="btn btn-primary">Sign in</button>
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
import Camera from "../components/Camera.vue";
import ErrorMessageModal from "@/components/modals/ErrorMessageModal";

import { mapActions } from "vuex";
import authService from "@/service/AuthService";

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
        image: "",
      },
      errorMessage:
        "Unable to sign in. Username or password is invalid. Please try again.",
    };
  },
  methods: {
    ...mapActions(["setUser"]),
    async login() {
      try {
        this.payload.image = await this.$refs.cam.getBase64Face();
        const response = await authService.login(this.payload);
        this.setUser(response.data);
        this.$router.push({ name: "Home" });
        alert("Loggin success");
      } catch (error) {
        if (error.response) {
          this.$refs.errorModalRef.showModal();
        }
      }
    },
  },
};
</script>
