//__Developed__ by PUNIT 06-03-2026
sap.ui.define(
    ["sap/ui/core/mvc/Controller",
     "jquery.sap.global",
     "punit/util/service",
     "sap/m/MessageBox"],
    function(Controller, jQuery, service, MessageBox){
        return Controller.extend("punit.controller.Customer", {
            onInit: function(){
                var oModel = new sap.ui.model.json.JSONModel();
                oModel.setData({
                    "postPayload": {
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": "",
                        "address": ""
                    },
                    "editPayload": {
                        "customerId": "",
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": "",
                        "address": ""
                    },
                    "customer": [],
                    "editMode": false,
                    "searchCustomerId": "",
                    "showOperationSelector": true,
                    "showCreatePanel": false,
                    "showReadPanel": false,
                    "showUpdatePanel": false,
                    "showDeletePanel": false
                });
                this.getView().setModel(oModel);
            },

            // Operation Selection Methods
            onSelectCreate: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreatePanel", true);
                oModel.setProperty("/showReadPanel", false);
                oModel.setProperty("/showUpdatePanel", false);
                oModel.setProperty("/showDeletePanel", false);
            },

            onSelectRead: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreatePanel", false);
                oModel.setProperty("/showReadPanel", true);
                oModel.setProperty("/showUpdatePanel", false);
                oModel.setProperty("/showDeletePanel", false);
                // ✅ Auto-load data
                this.onLoadData();
            },

            onSelectUpdate: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreatePanel", false);
                oModel.setProperty("/showReadPanel", false);
                oModel.setProperty("/showUpdatePanel", true);
                oModel.setProperty("/showDeletePanel", false);
                // Reset edit mode and search fields
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchCustomerId", "");
            },

            onSelectDelete: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreatePanel", false);
                oModel.setProperty("/showReadPanel", false);
                oModel.setProperty("/showUpdatePanel", false);
                oModel.setProperty("/showDeletePanel", true);
                // ✅ Auto-load data for delete
                this.onLoadDataForDelete();
            },

            onBack: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", true);
                oModel.setProperty("/showCreatePanel", false);
                oModel.setProperty("/showReadPanel", false);
                oModel.setProperty("/showUpdatePanel", false);
                oModel.setProperty("/showDeletePanel", false);
                // Reset all form data
                this.resetAllData();
            },

            // CREATE - Save new Customer
            onSave: function(){
                var oModel = this.getView().getModel();
                var payload = oModel.getProperty("/postPayload");

                // Validation
                if(!payload.name || !payload.city || !payload.state){
                    MessageBox.error("Please fill all required fields (Name, City, State)");
                    return;
                }

                service.callService("/customer", "POST", payload)
                .then(function(response){
                    MessageBox.success("Customer Created Successfully");
                    // Reset form
                    oModel.setProperty("/postPayload", {
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": "",
                        "address": ""
                    });
                    // Go back to operation selector
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to create customer");
                    console.error(err);
                });
            },

            // READ - Load all Customers
            onLoadData: function(){
                var that = this;
                service.callService("/customer", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idCustomerTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/customer", data);
                    oTable.bindRows("/customer");
                    MessageBox.information("Data loaded successfully. Total records: " + data.length);
                })
                .catch(function(err){
                    MessageBox.error("Error loading data");
                    console.error(err);
                });
            },

            // DELETE - Load data for delete operation
            onLoadDataForDelete: function(){
                var that = this;
                service.callService("/customer", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idDeleteCustomerTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/customer", data);
                    oTable.bindRows("/customer");
                    MessageBox.information("Data loaded successfully. Total records: " + data.length);
                })
                .catch(function(err){
                    MessageBox.error("Error loading data");
                    console.error(err);
                });
            },

            // EDIT - Search and load customer by ID
            onEditCustomer: function(){
                var oModel = this.getView().getModel();
                // Get value directly from input field instead of model
                var oSearchInput = this.getView().byId("searchCustomerId");
                var customerId = oSearchInput ? oSearchInput.getValue() : "";

                // Validation
                if(!customerId || customerId.trim() === ""){
                    MessageBox.error("Please enter a Customer ID");
                    return;
                }

                service.callService("/customer/" + customerId, "GET", {})
                .then(function(data){
                    oModel.setProperty("/editPayload", {
                        "customerId": data.customerId,
                        "name": data.name,
                        "city": data.city,
                        "state": data.state,
                        "phone": data.phone,
                        "address": data.address
                    });
                    oModel.setProperty("/editMode", true);
                    MessageBox.information("Customer data loaded successfully");
                })
                .catch(function(err){
                    MessageBox.error("Error: Customer with ID " + customerId + " not found");
                    console.error(err);
                });
            },

            // UPDATE - Save edited customer
            onUpdateCustomer: function(){
                var oModel = this.getView().getModel();
                var editPayload = oModel.getProperty("/editPayload");

                // Validation
                if(!editPayload.name || !editPayload.city || !editPayload.state){
                    MessageBox.error("Please fill all required fields (Name, City, State)");
                    return;
                }

                // Remove customerId from payload for update
                var updatePayload = {
                    "name": editPayload.name,
                    "city": editPayload.city,
                    "state": editPayload.state,
                    "phone": editPayload.phone,
                    "address": editPayload.address
                };

                service.callService("/customer/" + editPayload.customerId, "PUT", updatePayload)
                .then(function(response){
                    MessageBox.success("Customer Updated Successfully");
                    // Reset edit form and go back
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to update customer");
                    console.error(err);
                });
            },

            // Cancel edit mode
            onCancelEdit: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchCustomerId", "");
                oModel.setProperty("/editPayload", {
                    "customerId": "",
                    "name": "",
                    "city": "",
                    "state": "",
                    "phone": "",
                    "address": ""
                });
            },

            // DELETE - Delete selected customers
            onDeleteSelected: function(){
                var that = this;
                var oTable = this.getView().byId("idDeleteCustomerTable");
                var aSelectedIndices = oTable.getSelectedIndices();

                if(aSelectedIndices.length === 0){
                    MessageBox.warning("Please select at least one row to delete");
                    return;
                }

                MessageBox.confirm("Are you sure you want to delete " + aSelectedIndices.length + " selected record(s)?", {
                    onClose: function(oAction){
                        if(oAction === "OK"){
                            var deletePromises = [];
                            
                            aSelectedIndices.forEach(function(iIndex){
                                var oContext = oTable.getContextByIndex(iIndex);
                                if(oContext){
                                    var oObject = oContext.getObject();
                                    var customerId = oObject.customerId;
                                    deletePromises.push(
                                        service.callService("/customer/" + customerId, "DELETE", {})
                                    );
                                }
                            });

                            // Execute all delete operations
                            Promise.all(deletePromises)
                            .then(function(){
                                MessageBox.success("Customer(s) deleted successfully");
                                that.onBack();
                            })
                            .catch(function(err){
                                MessageBox.error("Error deleting customer(s)");
                                console.error(err);
                            });
                        }
                    }
                });
            },

            // Reset all data
            resetAllData: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/postPayload", {
                    "name": "",
                    "city": "",
                    "state": "",
                    "phone": "",
                    "address": ""
                });
                oModel.setProperty("/editPayload", {
                    "customerId": "",
                    "name": "",
                    "city": "",
                    "state": "",
                    "phone": "",
                    "address": ""
                });
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchCustomerId", "");
            }
        });
    }
);