import axios from "axios";
import Auth from "auth"

// For local development, this is hardcoded in at the moment
const AUTH_URL = "http://localhost:8080/api/v1/auth/";
const SERVICE_URL = "http://localhost:8080/api/v1/service/";
const BOOKING_URL = "http://localhost:8080/api/v1/auth/";

class Service {
    bookService(businessID, serviceID, workerID, dateTime) {
        const currentUser = Auth.getCurrentUser();
        return axios
            .post(BOOKING_URL + "bookService", {
                businessID,
                serviceID,
                workerID,
                dateTime,
                // currentUser.userID
            })
    }

    // Probably an incorrect place to put this
    // getBusinessesAll() {
    //     return
    // }

    getServicesByBusinessID(businessID) {
        return axios.get(SERVICE_URL, businessID);
    }

}

export default new Service();