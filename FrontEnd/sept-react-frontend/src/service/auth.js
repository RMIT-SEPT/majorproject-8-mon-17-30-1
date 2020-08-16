import axios from "axios";

// For local development, this is hardcoded in at the moment
const API_URL = "http://localhost:8080/api/v1/auth/";

class Auth {
  login(username, password) {
    return axios
      .post(API_URL + "login", {
        username,
        password
      })
      .then(response => {
        if (response.data.token) {
          localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem("user");
  }

  register(username, password, firstName, lastName, streetAddress, city, state, postcode) {
    return axios.post(API_URL + "register", {
      username,
      password,
      firstName,
      lastName,
      streetAddress,
      city,
      state,
      postcode
    });
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));;
  }
}

export default new Auth();