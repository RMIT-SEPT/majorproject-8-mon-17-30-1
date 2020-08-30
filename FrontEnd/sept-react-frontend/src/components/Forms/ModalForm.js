import React, { Component } from 'react';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Progress
} from 'reactstrap';
/* eslint-disable */
class ModalForm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      inputs: [],
      values: {}
    };
  }

  //------------------------------------
  handleInputChange = event => {
    const target = event.target;
    const value = target.value;
    const name = target.name;

    const values = this.state.values;
    values[name] = value;
    this.setState({
      values
    });
  };

  //------------------------------------
  onSubmit = () => {
    this.props.onSubmit(this.state.values);
  };

  //------------------------------------
  componentDidMount() {
    const inputs = this.props.inputs;
    // const inputs = this.props.inputs.map(input => (
    //   <div key={input.name}>
    //     <label>
    //       {input.displayName}:
    //       <input
    //         type={input.type}
    //         name={input.name}
    //         value={this.state.values[input.name]}
    //         onChange={this.handleInputChange}
    //         {...input.attributes}
    //       />
    //     </label>
    //   </div>
    // ));

    this.setState({
      inputs
    });
  }

  //------------------------------------
  componentWillReceiveProps(nextProps) {
    if (this.props.progress) {
      const progressBars = this.props.progress.map((item, index) => {
        if (item.progress < 100 || item.progress > 0) {
          return (
            <div key={index}>
              <Progress
                key={index}
                animated
                color="success"
                value={item.progress}
              >
                {item.progress}%
              </Progress>
              <p>{item.id}</p>
            </div>
          );
        }
        return null;
      });
      this.setState({ progressBars });
    } else {
      this.setState({ progressBars: [] });
    }
  }

  //------------------------------------
  componentWillUnmount() {
    this.isCancelled = true;
  }

  render() {
    const { title, isOpen, onClose, onCancel } = this.props;

    return (
      <Modal onClose={onClose} isOpen={isOpen}>
        <ModalHeader>
          <strong>{title}</strong>
        </ModalHeader>

        <ModalBody>
          <form>{this.state.inputs}</form>
          <div>{this.state.progressBars}</div>
        </ModalBody>

        <ModalFooter>
          <Button color="success" onClick={this.onSubmit}>
            Submit
          </Button>
          <Button color="danger" onClick={onCancel}>
            Cancel
          </Button>
        </ModalFooter>
      </Modal>
    );
  }
}

export default ModalForm;
