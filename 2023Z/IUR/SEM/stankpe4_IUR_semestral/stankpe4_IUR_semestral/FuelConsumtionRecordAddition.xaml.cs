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
using System.Windows.Shapes;
using SemesterWork___car_data_database.Models;

namespace SemesterWork___car_data_database
{
    /// <summary>
    /// Interakční logika pro FuelConsumtionRecordAddition.xaml
    /// </summary>
    public partial class FuelConsumtionRecordAddition : Window
    {
        float? backupAmountRefueled = null;
        float? backupDistanceTraveled = null;
        int? backupPrice = null;
        string backupDate = DateTime.Now.Date.ToString();
        string backupNotes = string.Empty;
        bool useBackups = false;

        public FuelConsumtionRecordAddition(FuelRecordsViewModel fuelRecords, FuelRecordsDataModel dataToEdit, bool editingRecord)
        {
            InitializeComponent();
            // set data for viewModel
            if(dataToEdit != null) 
            {
                this.DataContext = dataToEdit;
                ((FuelRecordsDataModel)(this.DataContext)).EditingRecord = editingRecord;
                // set for possible cancelation of changes
                backupAmountRefueled = dataToEdit.AmountRefueled;
                backupDistanceTraveled = dataToEdit.DistanceTraveled;
                backupPrice = dataToEdit.Price;
                backupDate = dataToEdit.Date;
                backupNotes = dataToEdit.AdditionNote;
                useBackups = editingRecord;
            }
            else
            {
                ((FuelRecordsDataModel)(this.DataContext)).FuelRecordsViewModel = fuelRecords;
                ((FuelRecordsDataModel)(this.DataContext)).EditingRecord = editingRecord;
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
            if (useBackups)
            {
                ((FuelRecordsDataModel)(this.DataContext)).AdditionNote = backupNotes;
                ((FuelRecordsDataModel)(this.DataContext)).Price = backupPrice;
                ((FuelRecordsDataModel)(this.DataContext)).Date = backupDate;
                ((FuelRecordsDataModel)(this.DataContext)).AmountRefueled = backupAmountRefueled;
                ((FuelRecordsDataModel)(this.DataContext)).DistanceTraveled = backupDistanceTraveled;
            }
            // just close window
            DialogResult = false;
        }
    }
}
