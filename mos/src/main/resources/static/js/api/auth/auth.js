
export default {
    modalHtml: async () => {
        return await axios.get('/auth/login-template',{
            responseType: 'document'
        })
    },
    signUp: async params => {
        return await axios.post('/api/v1/member/add', params, {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        });
    },
    findByEmail: async params => {
        return await axios.post('/api/v1/member/findByUsername', params);
    },
    logout: async () => {
        return await axios.post('/auth/logout');
    }


}
