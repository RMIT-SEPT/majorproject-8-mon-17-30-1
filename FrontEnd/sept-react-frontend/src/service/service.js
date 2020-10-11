import axios from "axios";
import authHeader from './auth-header';

// For local development, this is hardcoded in at the moment
export const SERVICE_URL = "http://localhost:8080/api/v1/service/";
export const BOOKING_URL = "http://localhost:8080/api/v1/booking/";
export const BUSINESS_URL = "http://localhost:8080/api/v1/business/";
export const WORKER_URL = "http://localhost:8080/api/v1/worker/";

class Service {
    bookService(serviceId, workerId, bookingTime, customerUsername) {
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
        return axios
            .delete(BOOKING_URL + "cancel/" + bookingID, {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            });
    }

    deleteService(serviceId) {
        return axios
            .delete(SERVICE_URL + "delete/" + serviceId, {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            });
    }

    getBusinessesAll() {
        return axios
            .get(BUSINESS_URL, {
                headers: authHeader()
            });
    }

    getServicesByBusinessID(businessId) {
        return axios
            .get(SERVICE_URL, { params:
                {"business-id": businessId},
                headers: authHeader()
            });
    }

    getServicesByWorkerId(workerId) {
        return axios
            .get(SERVICE_URL + "service", { params:
                {"worker-id": workerId},
                headers: authHeader()
            });
    }

    getBusinessByAdminUsername(username) {
        return axios
            .get(BUSINESS_URL + "admin", {params:
                {"username": username},
                headers: authHeader()
            });
    }

    getWorkersByServiceID(serviceID) {
        return axios
            .get(WORKER_URL, {params:
                {"service-id": serviceID},
                headers: authHeader()
            });
    }

    createService(businessId, serviceName, durationMinutes) {
        return axios
            .post(SERVICE_URL,{
                businessId,
                serviceName,
                durationMinutes
            }, {
                headers: authHeader()
            });
    }

}

export default new Service();