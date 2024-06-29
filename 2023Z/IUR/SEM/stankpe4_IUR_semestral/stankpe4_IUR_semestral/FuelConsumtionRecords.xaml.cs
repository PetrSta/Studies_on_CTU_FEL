using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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
    /// Interakční logika pro FuelConsumtion.xaml
    /// </summary>
    public partial class FuelConsumtionRecords : Window
    {
        public FuelConsumtionRecords(MainWindowViewModel mainWindow, ObservableCollection<FuelRecordsDataModel> fuelList)
        {
            InitializeComponent();
            // set data for viewModel
            ((FuelRecordsViewModel)(this.DataContext)).MainWindowViewModel = mainWindow;
            ((FuelRecordsViewModel)(this.DataContext)).FuelRefuelList.Clear();
            // copy saved list from main window
            for (int i = 0; i < fuelList.Count; i++)
            {
                ((FuelRecordsViewModel)(this.DataContext)).FuelRefuelList.Add(fuelList[i]);
            }
        }

        private void FuelConsumptionAddButtonClick(object sender, RoutedEventArgs e)
        {
            // open window to add new records
            var fuelConsumptionAdditionWindow = new FuelConsumtionRecordAddition((FuelRecordsViewModel)this.DataContext, null, false);
            fuelConsumptionAdditionWindow.ShowDialog();
        }

        private void OkButtonClick(object sender, RoutedEventArgs e)
        {
            // just close window
            DialogResult = true;
        }

        private void CancelButtonClick(object sender, RoutedEventArgs e)
        {
            // just close window
            DialogResult = false;
        }

        private void DoubleClick(object sender, RoutedEventArgs e)
        {
            // open window to edit existing record -> same view model as for addition of record but with different args
            var selectedData = ((FuelRecordsViewModel)(this.DataContext)).SelectedFuelConsumptionData;
            var fuelConsumptionEditingWindow = new FuelConsumtionRecordAddition((FuelRecordsViewModel)this.DataContext, selectedData, true);
            fuelConsumptionEditingWindow.ShowDialog();
        }
    }
}
