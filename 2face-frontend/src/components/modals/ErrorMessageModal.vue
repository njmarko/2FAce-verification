<template>
  <b-modal v-bind:id="id" hide-footer title="Dial-Up Error">
    <template #default="{}">
      <div class="row">
        <div class="col-4">
          <img
            alt="Warning icon"
            src="assets/icons/msg_warning-0.png"
            class="mx-auto d-block text-center"
            height="65"
          />
        </div>
        <div class="col-8">
          <p>
            {{ errorMessage }}
          </p>
        </div>
      </div>
      <div class="form-group row">
        <div class="col-sm-12 text-center">
          <b-button class="btn btn-primary w-25" @click="closeModal()"
            >OK</b-button
          >
        </div>
      </div>
    </template>
  </b-modal>
</template>

<script>
export default {
  props: ["id", "errorMessage"],
  methods: {
    showModal() {
      this.$bvModal.show(this.id);
    },
    closeModal() {
      this.$bvModal.hide(this.id);
    },
  },
};
</script>

<style scoped>
</style>