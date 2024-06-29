using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using SemesterWork___car_data_database.Models;


namespace SemesterWork___car_data_database
{
    /// <summary>
    /// Interakční logika pro ServiceRecordsAddition.xaml
    /// </summary>
    public partial class ServiceRecordsAddition : Window
    {
        bool backupGreenCard = false;
        bool backupSTK = false;
        bool backupServiceFinished = false;
        string backupRecordTag = string.Empty;
        string backupNotes = string.Empty;
        string backupDate = DateTime.Now.Date.ToString();
        int? backupPrice = null;
        bool useBackups = false;

        public ServiceRecordsAddition(ServiceRecordsViewModel serviceRecords, ServiceRecordsDataModel dataToEdit, bool editingRecord)
        {
            InitializeComponent();
            // set data for view model
            if(dataToEdit != null)
            {
                this.DataContext = dataToEdit;
                ((ServiceRecordsDataModel)(this.DataContext)).EditingRecord = editingRecord;
                backupGreenCard = dataToEdit.GreenCard;
                backupSTK = dataToEdit.STK;
                backupServiceFinished = dataToEdit.ServiceFinished;
                backupRecordTag = dataToEdit.RecordTag;
                backupNotes = dataToEdit.ServiceNotes;
                backupDate = dataToEdit.Date;
                backupPrice = dataToEdit.Price;
                useBackups = editingRecord;
            }
            else
            {
                ((ServiceRecordsDataModel)(this.DataContext)).ServiceRecordsViewModel = serviceRecords;
                ((ServiceRecordsDataModel)(this.DataContext)).EditingRecord = editingRecord;
            }
        }

        private void OkButtonClick(object sender, RoutedEventArgs e)
        {
            // just close window
            DialogResult = true;
        }

        private void CancelButtonClick(object sender, RoutedEventArgs e)
        {
            // revert changes from editing
            if(useBackups)
            {
                ((ServiceRecordsDataModel)(this.DataContext)).GreenCard = backupGreenCard;
                ((ServiceRecordsDataModel)(this.DataContext)).STK = backupSTK;
                ((ServiceRecordsDataModel)(this.DataContext)).ServiceFinished = backupServiceFinished;
                ((ServiceRecordsDataModel)(this.DataContext)).RecordTag = backupRecordTag;
                ((ServiceRecordsDataModel)(this.DataContext)).ServiceNotes = backupNotes;
                ((ServiceRecordsDataModel)(this.DataContext)).Date = backupDate;
                ((ServiceRecordsDataModel)(this.DataContext)).Price = backupPrice;
            }
            // just close window
            DialogResult = false;
        }
    }
}
