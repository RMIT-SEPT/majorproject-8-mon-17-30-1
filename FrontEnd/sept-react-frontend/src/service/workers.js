import axios from "axios";
import authHeader from "./auth-header";
import determineError from "./helper";

// For local development, this is hardcoded in at the moment
export const API_URL = "http://ec2-13-211-211-139.ap-southeast-2.compute.amazonaws.com:8080/api/v1/worker/";

class Workers {
    viewWorkers() {
        return axios
            .get(API_URL, {
                headers: authHeader(),
            })
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    newWorker(username, firstName, lastName) {
        return axios
            .post(
                API_URL + "create",
                {
                    username,
                    firstName,
                    lastName,
                },
                { headers: authHeader() }
            )
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    newAvailability(workerId, serviceId, day, startTime, endTime, effectiveEndDate) {
        return axios
            .post(
                API_URL + "availability",
                {
                    workerId,
                    serviceId,
                    day,
                    startTime,
                    endTime,
                    effectiveEndDate,
                },
                { headers: authHeader() }
            )
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    deleteWorker(workerId) {
        return axios
            .delete(API_URL + "delete/" + workerId, {
                headers: authHeader(),
            })
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    viewWorkerDateData(workerId) {
        return axios
            .get(API_URL + "availability/" + workerId, {
                headers: authHeader(),
            })
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }

    editWorker(username, password, firstName, lastName, workerId) {
        /*        console.log(username);
                console.log(password);
                console.log(firstName);
                console.log(lastName);
                console.log(workerId);*/
        return axios
            .put(
                API_URL + "edit",
                {
                    workerId,
                    username,
                    password,
                    firstName,
                    lastName,
                },
                { headers: authHeader() }
            )
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                determineError(error);
            });
    }
}

export default new Workers();
