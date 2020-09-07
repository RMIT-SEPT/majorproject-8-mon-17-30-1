import axios from "axios";
import authHeader from './auth-header';

// For local development, this is hardcoded in at the moment
const SERVICE_URL = "http://localhost:8080/api/v1/service/";
const BOOKING_URL = "http://localhost:8080/api/v1/booking/";

class Service {
    bookService(serviceID, workerID, dateTime, username) {
        return axios
            .post(BOOKING_URL + "create", {
                serviceID,
                workerID,
                dateTime,
                username
            }, {headers: authHeader()})
    }

    // Probably an incorrect place to put this
    getBusinessesAll() {
        return axios.get();
    }

    getServicesByBusinessID(businessID) {
        return axios.get(SERVICE_URL, businessID);
    }

    getWorkersByServiceID(serviceID) {

    }

}

export default new Service();