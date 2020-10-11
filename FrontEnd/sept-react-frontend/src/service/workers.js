import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
export const API_URL = "http://localhost:8080/api/v1/worker/";

class Workers {
    viewWorkers() {
        return axios
            .get(API_URL, {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            });
    }

    newWorker(username, firstName, lastName) {
        return axios
            .post(API_URL + "create", {
                username,
                firstName,
                lastName
            }, {headers: authHeader()})
            .then(response => {
                return response.data;
            });
    }

    newAvailability(workerId, serviceId, day, startTime, endTime, effectiveEndDate) {
        return axios
            .post(API_URL + "availability", {
                workerId,
                serviceId,
                day,
                startTime,
                endTime,
                effectiveEndDate
            }, {headers: authHeader()})
            .then(response => {
                return response.data;
            });
    }

    deleteWorker(workerId) {
        return axios
            .delete(API_URL + "delete/" + workerId, {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            });
    }
}

export default new Workers();