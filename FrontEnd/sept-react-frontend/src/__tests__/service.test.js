import axios from 'axios';

import Service, {BOOKING_URL, BUSINESS_URL, SERVICE_URL, WORKER_URL} from '../service/service';

jest.mock('axios');

// Related Acceptance test ID:
describe('Service.bookService', () => {
    it('successfully books service', async () => {
        const serviceId = "0"
        const workerId = "0"
        const bookingTime = "fakeTime"
        const customerUsername = "user"
        const data = {
            data: {}
        }

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        await Service.bookService(serviceId, workerId, bookingTime, customerUsername).then( response => {
            expect(response.data).toEqual();
        });

        expect(axios.post).toHaveBeenCalledWith(
            `${BOOKING_URL + "create"}`, {
                "serviceId": serviceId,
                "workerId": workerId,
                "bookingTime": bookingTime,
                "customerUsername": customerUsername
            }, {"headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const serviceId = "0"
        const workerId = "0"
        const bookingTime = "fakeTime"
        const customerUsername = "user"
        const errorMessage = 'Network Error';

        axios.post.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.bookService(serviceId, workerId, bookingTime, customerUsername)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.cancelBooking', () => {
    it('successfully cancel booking', async () => {
        const bookingId = "0"
        const data = {
            data: {}
        }

        axios.delete.mockImplementationOnce(() => Promise.resolve(data));

        await Service.cancelBooking(bookingId).then( response => {
            expect(response.data).toEqual();
        });

        expect(axios.delete).toHaveBeenCalledWith(
            `${BOOKING_URL + "cancel/" + bookingId}`, {"headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const bookingId = "0"
        const errorMessage = 'Network Error';

        axios.delete.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.cancelBooking(bookingId)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.deleteService', () => {
    it('successfully delete service', async () => {
        const serviceId = "0"
        const data = {
            data: {}
        }

        axios.delete.mockImplementationOnce(() => Promise.resolve(data));

        await Service.deleteService(serviceId).then( response => {
            expect(response.data).toEqual();
        });

        expect(axios.delete).toHaveBeenCalledWith(
            `${SERVICE_URL + "delete/" + serviceId}`, {"headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const serviceId = "0"
        const errorMessage = 'Network Error';

        axios.delete.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.deleteService(serviceId)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.getBusinessesAll', () => {
    it('successfully calls the API', async () => {
        const data = {
            data: {
                businesses: [
                    {
                        businessId: '1',
                        businessName: 'a',
                    },
                    {
                        businessId: '2',
                        businessName: 'b',
                    },
                ],
            },
        };

        axios.get.mockResolvedValue(data);

        await Service.getBusinessesAll().then( response => {
            expect(response).toEqual(data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${BUSINESS_URL}`, {"headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const errorMessage = 'Network Error';

        axios.get.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.getBusinessesAll()).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.getServicesByBusinessID', () => {
    it('successfully calls the API for services', async () => {
        const businessId = 1;

        const data = {
            data: {
                services: [
                    {
                        serviceId: '1',
                        serviceName: 'a'
                    },
                ]
            }
        };

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await expect(Service.getServicesByBusinessID(businessId)).resolves.toEqual(data);

        expect(axios.get).toHaveBeenCalledWith(
            `${SERVICE_URL}`,
            {"headers": {},
                "params": {
                    "business-id": 1,
                }},
        );
    });

    it('erroneously calls API', async () => {
        const errorMessage = 'Network Error';

        const businessId = 1;

        axios.get.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.getServicesByBusinessID(businessId)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.getBusinessByAdminUsername', () => {
    it('successfully get businesses by admin username', async () => {
        const username = "user"
        const data = {
            data: {
                businesses: [
                    {
                        businessId: '1',
                        businessName: 'a',
                    },
                    {
                        businessId: '2',
                        businessName: 'b',
                    },
                ],
            },
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Service.getBusinessByAdminUsername(username).then(response => {
            expect(response).toEqual(data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${BUSINESS_URL + "admin"}`, {params: {"username": username}, "headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const username = "user"
        const errorMessage = 'Network Error';

        axios.get.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.getBusinessByAdminUsername(username)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.getWorkersByServiceID', () => {
    it('successfully get workers by service id', async () => {
        const serviceId = "0"
        const data = {
            data: {
                workers: [
                    {
                        workerId: '1',
                        workerName: 'worker1',
                    },
                    {
                        workerId: '2',
                        workerName: 'worker2',
                    },
                ],
            },
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Service.getWorkersByServiceID(serviceId).then(response => {
            expect(response).toEqual(data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${WORKER_URL}`, {params: {"service-id": serviceId}, "headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const serviceId = "0"
        const errorMessage = 'Network Error';

        axios.get.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.getWorkersByServiceID(serviceId)).rejects.toThrow(errorMessage);
    });
});

// Related Acceptance test ID:
describe('Service.createService', () => {
    it('successfully create service', async () => {
        const businessId = "0"
        const serviceName = "service"
        const durationMinutes = "30"
        const data = {
            data: {}
        }

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        await Service.createService(businessId, serviceName, durationMinutes).then(response => {
            expect(response).toEqual(data);
        });

        expect(axios.post).toHaveBeenCalledWith(
            `${SERVICE_URL}`, {
                businessId,
                serviceName,
                durationMinutes
            }, {"headers": {}}
        );
    });

    it('erroneously calls API', async () => {
        const businessId = "0"
        const serviceName = "service"
        const durationMinutes = "30"
        const errorMessage = 'Network Error';

        axios.post.mockImplementationOnce(() =>
            Promise.reject(new Error(errorMessage)),
        );

        await expect(Service.createService(businessId, serviceName, durationMinutes)).rejects.toThrow(errorMessage);
    });
});