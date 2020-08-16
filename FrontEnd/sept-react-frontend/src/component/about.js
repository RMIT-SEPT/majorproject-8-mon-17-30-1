import React, { Component } from "react";


export default class About extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>About us</h3>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin congue pulvinar purus, at iaculis justo lacinia id. Vestibulum feugiat nunc lectus, vel iaculis nibh semper id. Pellentesque non elit tempor, congue diam lacinia, varius elit. Donec libero ipsum, posuere ullamcorper lobortis non, suscipit eget lacus. Cras euismod mi tortor, efficitur gravida lacus viverra sed. Nullam laoreet euismod urna non consequat. Suspendisse efficitur, nulla a sagittis laoreet, nisi nisi gravida tortor, in efficitur risus tortor sed diam. Suspendisse mattis nunc nibh, a laoreet tellus lobortis a. Morbi fermentum gravida libero, non elementum nulla congue at. Suspendisse tortor dui, aliquam ac eleifend a, dapibus eu tellus. Etiam non hendrerit mauris.
          </p>

          <p>
            Proin non convallis ante. Donec mattis quam sit amet tellus posuere porta. Mauris id orci eros. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla eu feugiat diam. Suspendisse malesuada felis enim, nec viverra ex cursus eget. Sed aliquam gravida nulla, at ultrices metus fringilla sit amet. Nulla pharetra semper facilisis. Suspendisse vitae est vel metus aliquet ultrices. Pellentesque iaculis, nisi ullamcorper sollicitudin egestas, libero metus luctus urna, ac pellentesque eros magna vel risus. Integer id enim placerat, cursus est in, malesuada nibh. Vivamus sit amet semper diam.
          </p>
        </header>
      </div>
    );
  }
}
