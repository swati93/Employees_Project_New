import React, { Component } from 'react';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import axios, {put} from 'axios';

 function parseData(row) {
     return {
         id: row['id'],
         name: row['name'],
         department: row['department'],
         designation: row['designation'],
         salary: row['salary'],
         joiningDate: row['joiningDate']
     }
  }
  function onAfterSaveCell(row, cellName, cellValue) {
     alert(`Save cell ${cellName} with value ${cellValue}`);
     let rowStr = '';
     let id = row['id'];
     for (const prop in row) {
         rowStr += prop + ': ' + row[prop] + '\n';
     }
     alert('The whole row :\n' + rowStr);
     let fileData = parseData(row);
     axios.put("http://localhost:8080/employees/" + id, fileData).then(res => {
         alert("UPDATED SUCCESSFULLY.\nPlease..... Refresh the page to see actual data")
      },
      err => {
          alert("INVALID ENTRY. \nPlease..... Refresh the page to see actual data");
      });
  }

  const cellEditProp = {
      mode: 'click',
      blurToSave: true,
      afterSaveCell: onAfterSaveCell
  }

class TabularData extends Component {
  // initially data is empty in state
  constructor() {
      super();
      this.state = {
          data: []
      };
  }

  /* when component mounted, start a GET request
     to specified URL */
  componentDidMount() {
    axios.get('http://localhost:8080/employees').then(response => {
        this.setState({
            data: response.data})
        })
  }

  render() {
     return (
         <div>
            <BootstrapTable data={ this.state.data }  pagination cellEdit={cellEditProp} >
                <TableHeaderColumn dataField= 'id' dataAlign="center" isKey={ true}>ID</TableHeaderColumn>
                <TableHeaderColumn dataField= 'name' editable={ { type: 'textarea' }} dataAlign="center">Name</TableHeaderColumn>
                <TableHeaderColumn dataField= 'department' editable={ { type: 'textarea' } } dataAlign="center">Department</TableHeaderColumn>
                <TableHeaderColumn dataField= 'designation' editable={ { type: 'textarea' } } dataAlign="center">Designation</TableHeaderColumn>
                <TableHeaderColumn dataField= 'salary' editable={ { type: 'textarea' } } dataAlign="center">Salary</TableHeaderColumn>
                <TableHeaderColumn dataField= 'joiningDate' dataAlign="center">Joining date</TableHeaderColumn>
            </BootstrapTable>
         </div>
     );
  }
}
export default TabularData;