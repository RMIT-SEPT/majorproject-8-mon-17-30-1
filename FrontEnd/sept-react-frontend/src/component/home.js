import React, { Component } from "react";
import Work from "../component/workerlist"
import Book from "../component/bookservice"

// import UserService from "../services/user.service";

export default class Home extends Component {
  constructor(props) {
    super(props);

    // this.state = {
    //   content: ""
    // };
  }

//   componentDidMount() {
//     UserService.getPublicContent().then(
//       response => {
//         this.setState({
//           content: response.data
//         });
//       },
//       error => {
//         this.setState({
//           content:
//             (error.response && error.response.data) ||
//             error.message ||
//             error.toString()
//         });
//       }
//     );
//   }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Welcome dean</h3>
          <Book />
        </header>
      </div>
    );
  }
}