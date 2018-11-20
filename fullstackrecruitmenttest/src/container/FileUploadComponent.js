import React, { Component } from 'react';

class FileUploadComponent extends Component {

    constructor(props) {
        super(props);
        this.onUpload = this.onUpload.bind(this);
    }

    onUpload(event) {
        event.stopPropagation();
        let files = event.target.files;
        if (files && (files.length === 1 && (files[0].type === 'text/csv' ||  files[0].name.endsWith('.csv')))) {
            this.props.parsedFile(files[0]);
        }
    }

    render() {
        return (
            <div>
                <input type="file"
                    onChange={this.onUpload}
                    accept="text/csv"></input>
            </div>
        );
    }
}
export default FileUploadComponent;