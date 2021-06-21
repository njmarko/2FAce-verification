import axios from "axios";
import ServiceBase from "@/service/ServiceBase";

class UserService extends ServiceBase {
	constructor() {
		super();
	}

	register(payload) {
		return axios.post(`${this.basePath}/users`, payload);
	}
}

export default new UserService();