import axios from "axios";
import determineError from "./helper";

// For local development, this is hardcoded in at the moment
export const API_URL = "http://ec2-13-211-211-139.ap-southeast-2.compute.amazonaws.com:8080/api/v1/auth";

const kebabcaseKeys = require("kebabcase-keys");

class Auth {
    login(username, password) {
        return axios
            .post(API_URL + "/login", {
                username,
                password,
            })
            .then((response) => {
                if (response.data.token) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(username, password, firstName, lastName, roleArgs) {
        return axios
            .post(
                API_URL + "/register",
                kebabcaseKeys(
                    {
                        username,
                        password,
                        firstName,
                        lastName,
                        roleArgs,
                    },
                    { deep: true }
                )
            )
            .then((response) => {
                if (response.data.token) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem("user"));
    }
}

export default new Auth();
