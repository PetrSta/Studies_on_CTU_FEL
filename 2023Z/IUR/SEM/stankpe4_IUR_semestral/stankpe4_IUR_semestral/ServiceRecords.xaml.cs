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
    /// Interakční logika pro ServiceRecords.xaml
    /// </summary>
    public partial class ServiceRecords : Window
    {
        public ServiceRecords(MainWindowViewModel mainWindow, ObservableCollection<ServiceRecordsDataModel> serviceList)
        {
            InitializeComponent();
            // set data for viewModel
            ((ServiceRecordsViewModel)(this.DataContext)).MainWindowViewModel = mainWindow;
            ((ServiceRecordsViewModel)(this.DataContext)).ServiceList.Clear();
            // copy list saved list from main window
            for (int i = 0; i < serviceList.Count; i++)
            {
                ((ServiceRecordsViewModel)(this.DataContext)).ServiceList.Add(serviceList[i]);
            }
        }

        private void ServiceAddButtonClick(object sender, RoutedEventArgs e)
        {
            // open window to add new service record
            var ServiceRecordsAdditionWindow = new ServiceRecordsAddition((ServiceRecordsViewModel)this.DataContext, null, false);
            ServiceRecordsAdditionWindow.ShowDialog();
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
            var selectedData = ((ServiceRecordsViewModel)(this.DataContext)).SelectedServiceData;
            var ServiceRecordsEdittingWindow = new ServiceRecordsAddition((ServiceRecordsViewModel)(this.DataContext), selectedData, true);
            ServiceRecordsEdittingWindow.ShowDialog();
        }
    }
}
