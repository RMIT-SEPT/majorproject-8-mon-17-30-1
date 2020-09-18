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
                    token: '1',
                    userName: 'John',
                    role: 'user'
                }
            }
        };

        axios.post.mockImplementationOnce(() => Promise.resolve(data));

        await expect(Auth.login(username, password)).toEqual(data);

        expect(axios.post).toHaveBeenCalledWith(
            `${API_URL}/login`,
        );
    });

    it('fails to call API for lack of input', async () => {
        const username = "";
        const password = "";

        const errorMessage = "Invalid Input";

        axios.post.mockImplementationOnce(() => Promise.reject(errorMessage));

        await expect(Auth.login(username, password)).rejects.toThrow(errorMessage);
    });
});