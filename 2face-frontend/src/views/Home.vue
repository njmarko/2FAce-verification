<template>
  <b-container id="homepage">
    <br />
    <Login />
  </b-container>
</template>

<script>
import Login from '../components/Login.vue';

export default {
  name: "Home",
  components: {
    Login
  },
   mounted() {
  },
  computed: {
  }
};
</script>
<style scoped>

</style>