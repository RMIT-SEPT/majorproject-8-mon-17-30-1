import axios from "axios";
import authHeader from './auth-header';

// For local development, this is hardcoded in at the moment
const SERVICE_URL = "http://localhost:8080/api/v1/service/";
const BOOKING_URL = "http://localhost:8080/api/v1/booking/";
const BUSINESS_URL = "http://localhost:8080/api/v1/business/";
const WORKER_URL = "http://localhost:8080/api/v1/worker/";

class Service {
    bookService(serviceId, workerId, bookingTime, customerUsername) {
        console.log(bookingTime);
        return axios
            .post(BOOKING_URL + "create", {
                serviceId,
                workerId,
                bookingTime,
                customerUsername
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

    async getServicesByBusinessID(businessId) {
        return await axios.get(SERVICE_URL, {params: {"business-id": businessId}, headers: authHeader()});
    }

    getWorkersByServiceID(serviceID) {
        return axios.get(WORKER_URL, {params: {"service-id": serviceID}, headers: authHeader()});
    }

}

export default new Service();