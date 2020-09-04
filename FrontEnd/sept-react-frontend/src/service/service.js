import axios from "axios";

// For local development, this is hardcoded in at the moment
const API_URL = "http://localhost:8080/api/v1/auth/";

class Service {
    bookService(service, worker, date, time) {
        return axios
            .post(API_URL + "bookService", {
                service,
                worker,
                date,
                time
            })
    }
}

export default new Service();