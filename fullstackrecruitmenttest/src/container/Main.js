import React, { Component } from 'react';
import axios, {post} from 'axios';
import csvParser from 'papaparse';
import FileUploadComponent from "./FileUploadComponent";
import TabularData from "./TabularData";
import fileDownload from 'js-file-download';
import './Main.css'

class Main extends Component {
    constructor(props) {
        super(props);
            this.state = {
                data: [],
                error: ""
            };
            this.onParseFile = this.onParseFile.bind(this);
            this.parseData = this.parseData.bind(this);
            this.renderError = this.renderError.bind(this);
    }

    parseData(data = []) {
                let filteredData = data.filter((entry, index) => {
                    if (entry.length !== 5) {
                        this.setState((state) => {
                            return {
                                data: state.data,
                                error: state.error + "\n data missing in line" + (index + 1)
                            }
                        });
                    }
                    return entry.length === 5;
                })

                return filteredData.map((entry, index) => {
                    return {
                        //id: index + 1,
                        name: entry[0],
                        department: entry[1],
                        designation: entry[2],
                        salary: entry[3],
                        joiningDate: entry[4]
                    }
                })
            }

    onDownloadFileError() {
        axios.get("http://localhost:8080/download").then(res => {
            fileDownload(res.data, "error.log");
        });
    }

    renderError() {
        if (this.state.error) {
            return (
                <div className="error-panel">
                    ERROR: {this.state.error}
                </div>
            )
        }
    }


    onParseFile(file) {
        csvParser.parse(file, {
            skipEmptyLines: true,
                complete: (result) => {
                    this.setState((state) => {
                        return {
                            data: state.data,
                            error: ""
                        }
                    });
                    let fileData = this.parseData(result.data);
                    if (!this.state.error) {
                        axios.post("http://localhost:8080/employees", fileData).then(res => {
                            this.setState((state) => {
                                return {
                                    data: res.data,
                                    error: ""
                                }
                            });
                            alert("SUCCESS: Refresh the page to see the data")
                        },
                        err => {
                            this.setState((state) => {
                                return {
                                    data: state.data,
                                    error: err.response && err.response.data ? err.response.data.message : ""
                                }
                            });
                        });
                    }
                },
                error: (error) => {
                    this.setState((state) => {
                        return {
                            data: state.data,
                            error: error
                        }
                    });
                    console.error('error parsing csv', this.state.error);
                }
            });
        }
        render() {
            return (
                <div className="Main">
                    <h1>Employees Records</h1>
                        <div>
                            <div className="">
                                <FileUploadComponent parsedFile={this.onParseFile} />
                            </div>
                            {this.renderError()}
                            <div className="download">
                                <button onClick={this.onDownloadFileError} download> Download File Error</button>
                            </div>
                            <TabularData />
                        </div>
                </div>
            );
        }
    }

export default Main;
