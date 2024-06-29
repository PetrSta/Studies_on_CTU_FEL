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

namespace IUR_TASK1
{
    /// <summary>
    /// Interakční logika pro ManageCities.xaml
    /// </summary>
    public partial class ManageCities : Window
    {

        // constructor
        public ManageCities(MainWindow mainWindow)
        {
            WeatherNet.Util.Api.ApiClient.ProvideApiKey("");
            InitializeComponent();
            foreach(ComboBoxItem item in mainWindow.MainWindowCombo.Items)
            {
                ListBoxItem listBoxItem = new ListBoxItem();
                listBoxItem.Content = item.Content;
                ManageCitiesList.Items.Add(listBoxItem);
            }
        }

        // function that handles logic of adding new city to list box
        private void AddCity()
        {
            string newCity = City_input.Text.Trim();
            //check if input has value in open weather api
            var data = WeatherNet.Current.GetByCityName(newCity, "Czechia", "en", "metric");
            
            // check if city is part of open weather map
            if (data.Item != null)
            {
                ListBoxItem item = new ListBoxItem();
                item.Content = newCity;
                bool duplicateFound = false;

                // check for duplicates
                foreach(ListBoxItem listItem in ManageCitiesList.Items)
                {
                    string city = listItem.Content.ToString();
                    if(city == null)
                    {
                        continue;
                    }
                    if(city.Equals(newCity, StringComparison.CurrentCultureIgnoreCase))
                    {
                        duplicateFound = true;
                    } 
                }

                if(!duplicateFound)
                {
                    ManageCitiesList.Items.Add(item);
                }
                else
                {
                    var duplicateErrorWindow = new DuplicateError_Window();
                    duplicateErrorWindow.ResizeMode = ResizeMode.NoResize;
                    duplicateErrorWindow.Left = this.Left + 125;
                    duplicateErrorWindow.Top = this.Top + 50;
                    
                    duplicateErrorWindow.ShowDialog();
                }
            }
            else
            {
                var unknownCityErrorWindow = new OpenWeatherAPI_Error_Window();
                unknownCityErrorWindow.ResizeMode = ResizeMode.NoResize;
                unknownCityErrorWindow.Left = this.Left + 125;
                unknownCityErrorWindow.Top = this.Top + 50;

                unknownCityErrorWindow.ShowDialog();
            }
            City_input.Clear();
        }

        // confirmation of changes using button
        private void OkButtonClick(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }

        // cancelation of changes using button
        private void CancelButtonClick(object sender, RoutedEventArgs e)
        {
            DialogResult = false;
        }

        // confirm addition of city using button
        private void AddCityButtonClick(object sender, RoutedEventArgs e)
        {
            if(City_input.Text.Length > 0)
            {
                AddCity();
            }
        }

        // confirm removal of city using button
        private void RemoveCityButtonClick(object sender, RoutedEventArgs e)
        {
            int selectedIndex = ManageCitiesList.SelectedIndex;
            ManageCitiesList.Items.Remove(ManageCitiesList.Items[selectedIndex]);
        }

        // confirm city addition using enter
        private void CityInputKeyPress(object sender, KeyEventArgs e)
        {
            if (City_input.Text.Length > 0)
            {
                if (e != null && e.Key == Key.Enter)
                {
                    AddCity();
                }
            }
        }

        // confirm city removal using delete
        private void ManageCitiesListKeyPress(object sender, KeyEventArgs e)
        {
            int selectedIndex = ManageCitiesList.SelectedIndex;
            if(e != null && selectedIndex != -1 && e.Key == Key.Delete)
            {
                ManageCitiesList.Items.Remove(ManageCitiesList.Items[selectedIndex]);
            }
        }
    }
}
