<template>
  <b-container id="homepage">
    <br />
    <Login />
  </b-container>
</template>

<script>
import Login from "../components/Login.vue";

export default {
  name: "Home",
  components: {
    Login,
  },
};
</script>
<style scoped>
</style>