<template>
<div class="container">
    <div class="card mb-4">
            <div class="card-header">
            <h4 class="my-0 font-weight-normal">Register</h4>
            </div>
            <div class="card-body">
                <form @submit.prevent="register">
                    <div class="form-group row">
                        <label for="inputUsername" class="col-sm-2 col-form-label"><img alt="Username icon" src="assets/icons/user_world-1.png" class="icon-16-4">Username</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-95" id="inputUsername" required
                                v-model="payload.username"
                            >
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputEmail" class="col-sm-2 col-form-label"><img alt="Email icon" src="assets/icons/envelope_closed-1.png" class="icon-16-4">Email</label>
                        <div class="col-sm-10">
                            <input type="email" class="form-95" id="inputEmail" required
                                v-model="payload.email"
                            >
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPassword3" class="col-sm-2 col-form-label"><img alt="Password icon" src="assets/icons/keys-0.png" class="icon-16-4"> Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-95" id="inputPassword3" required
                                v-model="payload.password" 
                            >
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPassword4" class="col-sm-2 col-form-label"><img alt="Confirm password icon" src="assets/icons/keys-0.png" class="icon-16-4"> Confirm password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-95" id="inputPassword4" required
                                v-model="payload.confirmPassword" 
                            >
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-12">
                            <button type="submit" class="btn btn-primary" v-b-modal.errMsgModal>Register</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>


<script>

import userService from "../service/UserService";

export default ({
	data() {
		return{
			payload: {
				username: "",
				password: "",
                email: "",
                confirmPassword: "",
			},
            showModal:false,
		}
	},
	methods: {
		async register() {
            if (this.payload.confirmPassword != this.payload.password) {
                return;
            }

            try {
                const res = await userService.register(this.payload);
                alert(`${res.data.username}, you have been registered successfully!`);
                this.$router.push({name: 'Login'});
            } catch (err) {
                console.log(err);
                alert("AN ERROR HAS OCCURED WHILE TRYING TO REGISTER.");
            }
        }
	}
})
</script>
