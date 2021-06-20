import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue"
import Login from "../components/Login.vue"
import Register from "../components/Register.vue";

Vue.use(VueRouter);

const routes = [
	{
		path: "/",
		name: "Home",
		component: Home,
		meta: {
			authenticated: false,
		}
	},
	{
		path: "/login",
		name: "Login",
		component: Login,
		meta: {
			authenticated: false,
		}
	},
	{
		path: "/register",
		name: "Register",
		component: Register,
		meta: {
			authenticated: false,
		}
	},
];

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
	if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
	return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
	routes,
});

export default router;

