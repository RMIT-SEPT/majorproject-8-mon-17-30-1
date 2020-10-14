export default function determineError(error) {
    if (error.response) {
        // Request made and server responded
        console.log(error.response.data);
        throw error.response.data.errors.map((error) => {
            return error.message;
        });
    } else if (error.request) {
        // client never received a response, or request never left
        console.log(error.request);
    } else {
        // anything else
        console.log("Error", error.message);
    }
}
