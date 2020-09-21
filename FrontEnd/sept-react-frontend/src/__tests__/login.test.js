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

        // expect(await Auth.login(username, password)).toEqual({"user": {"role": "user", "token": "1", "type": "Bearer", "username": "John"}});

        expect(await Auth.login(username, password)).toEqual(data.data);

        expect(axios.post).toHaveBeenCalledWith(
            `${API_URL}/login`, {"password": password, "username": username},
        );
    });
});