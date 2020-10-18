import axios from 'axios';

import Workers, { API_URL } from "../service/workers"
import authHeader from "../service/auth-header";
import Service, {BOOKING_URL} from "../service/service";

jest.mock('axios');

// Related Acceptance test ID: 4
describe('Workers.viewWorkers', () => {
    it('successfully get a list of workers', async () => {
        const data = {
            data: {
                workers: [
                    {
                        workerId: "0",
                        firstName: "worker0"
                    },
                    {
                        workerId: "1",
                        firstName: "worker1"
                    }
                ]
            }
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Workers.viewWorkers().then( response => {
            expect(response).toEqual(data.data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${API_URL}`, {"headers": {}}
        );
    });
});

// Related Acceptance test ID: 4
describe('Workers.newWorker', () => {
    it('successfully create a new worker', async () => {
        const username = "worker"
        const firstName = "user"
        const lastName = "name"
        const data = {
            data: {}
        }

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        await Workers.newWorker(username, firstName, lastName).then( response => {
            expect(response.data).toEqual();
        });

        expect(axios.post).toHaveBeenCalledWith(
            `${API_URL + "create"}`, {
                username,
                firstName,
                lastName
            }, {"headers": {}}
        );
    });
});

// Related Acceptance test ID: 24, 26
describe('Workers.deleteWorker', () => {
    it('successfully delete a worker', async () => {
        const workerId = "0"
        const data = {
            data: {}
        }

        axios.delete.mockImplementationOnce(() => Promise.resolve(data));

        await Workers.deleteWorker(workerId).then( response => {
            expect(response.data).toEqual();
        });

        expect(axios.delete).toHaveBeenCalledWith(
            `${API_URL + "delete/" + workerId}`, {"headers": {}}
        );
    });
});
