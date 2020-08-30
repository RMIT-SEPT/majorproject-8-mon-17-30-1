import React, { Component } from "react";

import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  FormGroup,
  Input,
  Label,
  Row
} from 'reactstrap';
import ModalForm from '../components/Forms/ModalForm';


export default class Bookings extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isOpenAdd: false,
      isOpenEdit: false,
      isOpenDelete: false
    };
  }

  onSubmit = async values => {
    
  };

  onCancel = () => {
    // Close Window
    const { onClose } = this.props;
    onClose();
  };

  onClose = () => {
    this.setState({
      isOpenAdd: false,
      isOpenEdit: false,
      isOpenDelete: false,
      title: ''
    });
  }

  setBookingTitle = () => {
    const { isOpenAdd } = this.state;
    this.setState({
      isOpenAdd: !isOpenAdd,
      title: 'Create Booking'
    });
  };

  setCancelTitle = () => {
    const { isOpenDelete } = this.state;
    this.setState({
      isOpenDelete: !isOpenDelete,
      title: 'Cancel Booking'
    });
  };

  toggleModal = () => {
    this.setState({
      isOpenAdd: false,
      isOpenEdit: false,
      isOpenDelete: false,
      title: ''
    });
  };

  addBooking = () => {
    const { isOpenAdd, title } = this.state;
    return (
      <ModalForm isOpen={isOpenAdd} onClose={this.toggleModal} title={title}>
        <FormGroup>
          <Label for="exampleEmail">Email</Label>
          <Input type="email" name="email" id="exampleEmail" placeholder="with a placeholder" />
        </FormGroup>
      </ModalForm>
    );
  };

  cancelBooking = () => {
    const { isOpenDelete, title } = this.state;
    return (
      <ModalForm
        isOpen={isOpenDelete}
        onClose={this.toggleModal}
        title={title}
      />
    );
  };

  render() {
    return (
      <div>
        <Row>
          <Col xl="2">
            <Card>
              <CardHeader>
                <strong>Bookings</strong>
              </CardHeader>
              <CardBody>
                <Row>
                  <Col>
                    <Row>
                      <Col col="6" sm="4" md="2" xl>
                        <Button
                          block
                          color="success"
                          onClick={this.setBookingTitle}
                        >
                          Create Booking
                        </Button>
                        {this.addBooking()}
                      </Col>
                      <Col col="6" sm="4" md="2" xl>
                        <Button
                          block
                          color="danger"
                          onClick={this.setCancelTitle}
                        >
                          Cancel Booking
                        </Button>
                        {this.cancelBooking()}
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}
