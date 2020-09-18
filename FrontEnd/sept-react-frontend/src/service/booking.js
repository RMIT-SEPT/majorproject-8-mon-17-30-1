import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
const API_URL = "http://localhost:8080/api/v1/booking/";

class Booking {
    viewBooking(username) {
        return axios
            .get(API_URL, {
                params: {
                    username: username
                },
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                console.log(error);
            });

    }
}

export default new Booking();