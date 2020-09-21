import axios from 'axios';

import Service, { BUSINESS_URL, SERVICE_URL } from '../service/service';

jest.mock('axios');

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

        // await expect(Service.getBusinessesAll().then(response => {
        //     return response;
        // })).resolves.toEqual(data);

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
        );
    });
});