//__Developed__ by PUNIT 06-03-2026
sap.ui.define(
    ["sap/ui/core/mvc/Controller",
     "jquery.sap.global",
     "punit/util/service",
     "sap/m/MessageBox"],
    function(Controller, jQuery, service, MessageBox){
        return Controller.extend("punit.controller.Product", {
            onInit: function(){
                var oModel = new sap.ui.model.json.JSONModel();
                oModel.setData({
                    "postPayload": {
                        "name": "",
                        "type": "",
                        "sector": "",
                        "group": "",
                        "uom": ""
                    },
                    "editPayload": {
                        "productId": "",
                        "name": "",
                        "type": "",
                        "sector": "",
                        "group": "",
                        "uom": ""
                    },
                    "product": [],
                    "editMode": false,
                    "searchProductId": "",
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
                oModel.setProperty("/searchProductId", "");
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

            // CREATE - Save new Product
            onSave: function(){
                var oModel = this.getView().getModel();
                var payload = oModel.getProperty("/postPayload");

                // Validation
                if(!payload.name || !payload.type || !payload.sector){
                    MessageBox.error("Please fill all required fields (Name, Type, Sector)");
                    return;
                }

                service.callService("/product", "POST", payload)
                .then(function(response){
                    MessageBox.success("Product Created Successfully");
                    // Reset form
                    oModel.setProperty("/postPayload", {
                        "name": "",
                        "type": "",
                        "sector": "",
                        "group": "",
                        "uom": ""
                    });
                    // Go back to operation selector
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to create product");
                    console.error(err);
                });
            },

            // READ - Load all Products
            onLoadData: function(){
                var that = this;
                service.callService("/product", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/product", data);
                    oTable.bindRows("/product");
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
                service.callService("/product", "GET", {})
                .then(function(data){
                    var oTable = that.getView().byId("idDeleteTable");
                    var oModel = that.getView().getModel();
                    oModel.setProperty("/product", data);
                    oTable.bindRows("/product");
                    MessageBox.information("Data loaded successfully. Total records: " + data.length);
                })
                .catch(function(err){
                    MessageBox.error("Error loading data");
                    console.error(err);
                });
            },

            // EDIT - Search and load product by ID
            onEditProduct: function(){
                var oModel = this.getView().getModel();
                var productId = oModel.getProperty("/searchProductId");

                // Validation
                if(!productId || productId.trim() === ""){
                    MessageBox.error("Please enter a Product ID");
                    return;
                }

                service.callService("/product/" + productId, "GET", {})
                .then(function(data){
                    oModel.setProperty("/editPayload", {
                        "productId": data.productId,
                        "name": data.name,
                        "type": data.type,
                        "sector": data.sector,
                        "group": data.group,
                        "uom": data.uom
                    });
                    oModel.setProperty("/editMode", true);
                    MessageBox.information("Product data loaded successfully");
                })
                .catch(function(err){
                    MessageBox.error("Error: Product with ID " + productId + " not found");
                    console.error(err);
                });
            },

            // UPDATE - Save edited product
            onUpdateProduct: function(){
                var oModel = this.getView().getModel();
                var editPayload = oModel.getProperty("/editPayload");

                // Validation
                if(!editPayload.name || !editPayload.type || !editPayload.sector){
                    MessageBox.error("Please fill all required fields (Name, Type, Sector)");
                    return;
                }

                // Remove productId from payload for update
                var updatePayload = {
                    "name": editPayload.name,
                    "type": editPayload.type,
                    "sector": editPayload.sector,
                    "group": editPayload.group,
                    "uom": editPayload.uom
                };

                service.callService("/product/" + editPayload.productId, "PUT", updatePayload)
                .then(function(response){
                    MessageBox.success("Product Updated Successfully");
                    // Reset edit form and go back
                    this.onBack();
                }.bind(this))
                .catch(function(err){
                    MessageBox.error("Error: Failed to update product");
                    console.error(err);
                });
            },

            // Cancel edit mode
            onCancelEdit: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchProductId", "");
                oModel.setProperty("/editPayload", {
                    "productId": "",
                    "name": "",
                    "type": "",
                    "sector": "",
                    "group": "",
                    "uom": ""
                });
            },

            // DELETE - Delete selected products
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
                                    var productId = oObject.productId;
                                    deletePromises.push(
                                        service.callService("/product/" + productId, "DELETE", {})
                                    );
                                }
                            });

                            // Execute all delete operations
                            Promise.all(deletePromises)
                            .then(function(){
                                MessageBox.success("Product(s) deleted successfully");
                                that.onBack();
                            })
                            .catch(function(err){
                                MessageBox.error("Error deleting product(s)");
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
                    "type": "",
                    "sector": "",
                    "group": "",
                    "uom": ""
                });
                oModel.setProperty("/editPayload", {
                    "productId": "",
                    "name": "",
                    "type": "",
                    "sector": "",
                    "group": "",
                    "uom": ""
                });
                oModel.setProperty("/editMode", false);
                oModel.setProperty("/searchProductId", "");
            }
        });
    }
);