import axios from "axios";
import ServiceBase from "@/service/ServiceBase";

class AuthService extends ServiceBase {
	constructor() {
		super();
	}

	login(payload) {
		return axios.post(`${this.basePath}/users/authenticate`, payload);
	}
}

export default new AuthService();