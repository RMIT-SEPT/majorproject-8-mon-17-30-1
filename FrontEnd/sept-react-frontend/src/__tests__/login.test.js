import axios from 'axios';

import Auth, { API_URL } from '../service/auth';

jest.mock('axios');

describe('Auth.login', () => {
    it('successfully logs into valid account', async () => {
        const username = "John";
        const password = "password";

        const data = {
            data: {
                user: {
                    type: 'Bearer',
                    token: '1',
                    username: 'John',
                    role: 'user',
                },
            },
        };

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        expect(await Auth.login(username, password)).toEqual(data.data);

        expect(axios.post).toHaveBeenCalledWith(
            `${API_URL}/login`, {"password": password, "username": username},
        );
    });
});

describe('Auth.register', () => {
    it('successfully register a customer', async () => {
        const username = "user"
        const password = "password"
        const firstName = "user"
        const lastName = "name"
        const roleArgs = "customer"
        const data = {
            data: {
                user: "user"
            }
        }

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        const expected = await Auth.register(username, password, firstName, lastName, roleArgs);
        expect(expected).toEqual(data.data);

        expect(axios.post).toHaveBeenCalledWith(
            `${API_URL}/register`, {
                "username": username,
                "password": password,
                "first-name": firstName,
                "last-name": lastName,
                "role-args": roleArgs,
            },
        );
    });
});