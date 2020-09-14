import axios from "axios";
import authHeader from './auth-header';

// For local development, this is hardcoded in at the moment
const SERVICE_URL = "http://localhost:8080/api/v1/service/";
const BOOKING_URL = "http://localhost:8080/api/v1/booking/";
const BUSINESS_URL = "http://localhost:8080/api/v1/business/";
const WORKER_URL = "http://localhost:8080/api/v1/worker/";

class Service {
    bookService(serviceID, workerID, dateTime, username) {
        return axios
            .post(BOOKING_URL + "create", {
                serviceID,
                workerID,
                dateTime,
                username
            }, {headers: authHeader()}).then(response => {
                return response.data;
            });
    }

    cancelBooking(bookingID) {
        return axios.delete(BOOKING_URL + "cancel/" + bookingID, {headers: authHeader()}).then(response => {
                return response.data;
            });
    }

    // Probably an incorrect place to put this
    async getBusinessesAll() {
        return await axios.get(BUSINESS_URL, {headers: authHeader()});
    }

    getServicesByBusinessID(businessID) {
        return axios.get(SERVICE_URL, {params: {businessID}, headers: authHeader()}).then(response => {
            return response.data;
        });
    }

    getWorkersByServiceID(serviceID) {
        return axios.get(WORKER_URL, {params: {serviceID}, headers: authHeader()}).then(response => {
            return response.data;
        });
    }

}

export default new Service();