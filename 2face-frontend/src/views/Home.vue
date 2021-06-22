<template>
  <b-container id="homepage">
    <br />
    <Login />
    <Camera />
  </b-container>
</template>

<script>
import Login from '../components/Login.vue';
import Camera from '../components/Camera.vue';

export default {
  name: "Home",
  components: {
    Login,
    Camera,
  },
   mounted() {
  },
  computed: {
  }
};
</script>
<style scoped>

</style>