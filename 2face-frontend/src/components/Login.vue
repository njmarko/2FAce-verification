<template>
<div class="container">
    <div class="card mb-4">
            <div class="card-header">
            <h4 class="my-0 font-weight-normal">Sign in</h4>
            </div>
            <div class="card-body">
                <form @submit.prevent="login">
                    <div class="form-group row">
                        <label for="inputUsername" class="col-sm-2 col-form-label"><img src="assets/icons/user_world-1.png" class="icon-16-4">Username</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-95" id="inputUsername"
                                v-model="payload.username"
                            >
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPassword3" class="col-sm-2 col-form-label"><img src="assets/icons/keys-0.png" class="icon-16-4"> Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-95" id="inputPassword3"
                                v-model="payload.password" 
                            >
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
 
        <b-modal id="errMsgModal" 
            hide-footer
            title="Dial-Up Error"
            >
                <template #default="{ }">
            <div class="row">
                <div class="col-4">
                    <img src="assets/icons/msg_warning-0.png" class="mx-auto d-block text-center" height="65" >
                </div>
                <div class="col-8">
                    <p>Unable to sign in. Username or password is invalid. Please try again.</p>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-12 text-center">
                    <b-button class="btn btn-primary w-25" @click="closeModal()">OK</b-button>
                </div>
            </div>
                </template>
        </b-modal>

    </div>
</template>


<script>

// import { makeToast} from "@/util/makeToast";
import { mapActions} from "vuex";
import authService from "../service/AuthService";

export default ({
	data() {
		return{
			payload:{
				username: "",
				password: "",
			},
		}
	},
	methods: {
		...mapActions(["setUser"]),
		login() {
			authService
			.login(this.payload)
			.then((response)=>{
				this.setUser(response.data);
				this.$router.push({name: "Home"});
                alert("Loggin success");
			})
			.catch((error)=>{
				if(error.response){
                    this.$bvModal.show("errMsgModal");
				}
			});
		},
        closeModal: function(){
            this.$bvModal.hide("errMsgModal");
        },
	}
})
</script>
