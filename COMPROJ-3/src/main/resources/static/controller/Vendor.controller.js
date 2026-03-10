//__Developed__ by PUNIT 06-03-2026
sap.ui.define(
    ["sap/ui/core/mvc/Controller",
     "jquery.sap.global",
     "punit/util/service",
     "sap/m/MessageBox"],
    function(Controller, jQuery, service, MessageBox){
        return Controller.extend("punit.controller.Vendor", {
            onInit: function(){
                var oModel = new sap.ui.model.json.JSONModel();
                oModel.setData({
                    "postPayload": {
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": ""
                    },
                    "editPayload": {
                        "vendorId": "",
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": ""
                    },
                    "vendor": [],
                    "editMode": false,
                    "searchVendorId": "",
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
                oModel.setProperty("/searchVendorId", "");
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

            // CREATE - Save new Vendor
            onSave: function(){
                var oModel = this.getView().getModel();
                var payload = oModel.getProperty("/postPayload");

                // Validation
                if(!payload.name || !payload.city || !payload.state || !payload.phone){
                    MessageBox.error("Please fill all required fields");
                    return;
                }

                service.callService("/vendors", "POST", payload)
                .then(function(response){
                    MessageBox.success("Vendor Created Successfully");
                    // Reset form
                    oModel.setProperty("/postPayload", {
                        "name": "",
                        "city": "",
                        "state": "",
                        "phone": ""
                    });
                    // Go back to operation selector
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to create vendor");
                    console.error(err);
                });
            },

            // READ - Load all Vendors
            onLoadData: function(){
                var that = this;
                service.callService("/vendors", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/vendor", data);
                    oTable.bindRows("/vendor");
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
                service.callService("/vendors", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idDeleteTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/vendor", data);
                    oTable.bindRows("/vendor");
                    MessageBox.information("Data loaded successfully. Total records: " + data.length);
                })
                .catch(function(err){
                    MessageBox.error("Error loading data");
                    console.error(err);
                });
            },

            // EDIT - Search and load vendor by ID
            onEditVendor: function(){
                var oModel = this.getView().getModel();
                // Get value directly from input field instead of model
                var oSearchInput = this.getView().byId("searchVendorId");
                var vendorId = oSearchInput ? oSearchInput.getValue() : "";

                // Validation
                if(!vendorId || vendorId.trim() === ""){
                    MessageBox.error("Please enter a Vendor ID");
                    return;
                }

                service.callService("/vendors/" + vendorId, "GET", {})
                .then(function(data){
                    oModel.setProperty("/editPayload", {
                        "vendorId": data.vendorId,
                        "name": data.name,
                        "city": data.city,
                        "state": data.state,
                        "phone": data.phone
                    });
                    oModel.setProperty("/editMode", true);
                    MessageBox.information("Vendor data loaded successfully");
                })
                .catch(function(err){
                    MessageBox.error("Error: Vendor with ID " + vendorId + " not found");
                    console.error(err);
                });
            },

            // UPDATE - Save edited vendor
            onUpdateVendor: function(){
                var oModel = this.getView().getModel();
                var editPayload = oModel.getProperty("/editPayload");

                // Validation
                if(!editPayload.name || !editPayload.city || !editPayload.state || !editPayload.phone){
                    MessageBox.error("Please fill all required fields");
                    return;
                }

                // Remove vendorId from payload for update
                var updatePayload = {
                    "name": editPayload.name,
                    "city": editPayload.city,
                    "state": editPayload.state,
                    "phone": editPayload.phone
                };

                service.callService("/vendors/" + editPayload.vendorId, "PUT", updatePayload)
                .then(function(response){
                    MessageBox.success("Vendor Updated Successfully");
                    // Reset edit form and go back
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to update vendor");
                    console.error(err);
                });
            },

            // Cancel edit mode
            onCancelEdit: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchVendorId", "");
                oModel.setProperty("/editPayload", {
                    "vendorId": "",
                    "name": "",
                    "city": "",
                    "state": "",
                    "phone": ""
                });
            },

            // DELETE - Delete selected vendors
            onDeleteSelected: function(){
                var that = this;
                var oTable = this.getView().byId("idDeleteTable");
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
                                    var vendorId = oObject.vendorId;
                                    deletePromises.push(
                                        service.callService("/vendors/" + vendorId, "DELETE", {})
                                    );
                                }
                            });

                            // Execute all delete operations
                            Promise.all(deletePromises)
                            .then(function(){
                                MessageBox.success("Vendor(s) deleted successfully");
                                that.onBack();
                            })
                            .catch(function(err){
                                MessageBox.error("Error deleting vendor(s)");
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
                    "phone": ""
                });
                oModel.setProperty("/editPayload", {
                    "vendorId": "",
                    "name": "",
                    "city": "",
                    "state": "",
                    "phone": ""
                });
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchVendorId", "");
            }
        });
    }
);