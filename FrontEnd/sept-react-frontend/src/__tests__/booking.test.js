import axios from 'axios';

import Booking, { API_URL } from "../service/booking"
import authHeader from "../service/auth-header";

jest.mock('axios');

describe('Booking.viewBooking', () => {
    it('successfully get list of active bookings for username', async () => {
        const username = "user"
        const data = {
            data: {
                bookings: {
                    bookingId: "0",
                    bookingState: "ACTIVE"
                }
            }
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Booking.viewBooking(username).then(response => {
            expect(response).toEqual(data.data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${API_URL + "viewActive"}`, {
                params: {
                    username: username
                },
                headers: authHeader()
            }
        );
    });
});

describe('Booking.viewBookingHistory', () => {
    it('successfully get list of inactive bookings for username', async () => {
        const username = "user"
        const data = {
            data: {
                bookings: {
                    bookingId: "0",
                    bookingState: "INACTIVE"
                }
            }
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Booking.viewBookingHistory(username).then( response => {
            expect(response).toEqual(data.data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${API_URL + "viewHistory"}`, {
                params: {
                    username: username
                },
                headers: authHeader()
            }
        );
    });
});

describe('Booking.viewAllBookingHistory', () => {
    it('successfully get ', async () => {
        const data = {
            data: {
                bookings: {
                    bookingId: "0",
                    bookingState: "INACTIVE"
                }
            }
        }

        axios.get.mockImplementationOnce(() => Promise.resolve(data));

        await Booking.viewAllBookingHistory().then( response => {
            expect(response).toEqual(data.data);
        });

        expect(axios.get).toHaveBeenCalledWith(
            `${API_URL + "viewAllHistory"}`, { headers: authHeader() }
        );
    });
});