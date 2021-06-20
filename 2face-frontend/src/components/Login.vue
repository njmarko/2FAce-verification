<template>
<div class="container">
    <div class="card mb-4">
            <div class="card-header">
            <h4 class="my-0 font-weight-normal">Sign in</h4>
            </div>
            <div class="card-body">
                <form>
                    <div class="form-group row">
                        <label for="inputEmail3" class="col-sm-2 col-form-label"><img src="assets/icons/envelope_closed-1.png" class="icon-16-4"> Email</label>
                        <div class="col-sm-10">
                            <input type="email" class="form-95" id="inputEmail3">
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="inputPassword3" class="col-sm-2 col-form-label"><img src="assets/icons/keys-0.png" class="icon-16-4"> Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-95" id="inputPassword3">
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-10">
                            <button type="submit" class="btn btn-primary">Sign in</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
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

			}
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
			})
			.catch((error)=>{
				if(error.response){
					// makeToast(this, "Error", "Wrong username or password.", "danger");
				}
			});
		}
	}
})
</script>
