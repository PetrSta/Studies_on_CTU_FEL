using IUR_TASK1;
using System;
using System.Globalization;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media.Imaging;

namespace IUR_P04_homework_template
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void AddInterest()
        {
            string newInterest = InterestTextBox.Text;
            ListBoxItem item = new ListBoxItem();
            item.Content = newInterest;
            bool duplicateFound = false;

            foreach(ListBoxItem listItem in InterestListBox.Items)
            {
                string interest = listItem.Content.ToString();
                if(interest == null )
                {
                    continue;
                }
                if(interest.Equals(newInterest, StringComparison.CurrentCultureIgnoreCase))
                {
                    duplicateFound = true;
                }
            }
            if(!duplicateFound) 
            {
                InterestListBox.Items.Add(item);
            }
            else
            {
                var unknownCityErrorWindow = new DuplicateError_Window();
                unknownCityErrorWindow.ResizeMode = ResizeMode.NoResize;
                unknownCityErrorWindow.Left = this.Left + (this.Width - unknownCityErrorWindow.Width)/2;
                unknownCityErrorWindow.Top = this.Top + (this.Height - unknownCityErrorWindow.Height)/2;

                unknownCityErrorWindow.ShowDialog();
            }
            InterestTextBox.Clear();
        }

        private void AddInterestButtonClick(object sender, RoutedEventArgs e)
        {
            if(InterestTextBox.Text != string.Empty)
            {
                AddInterest();
            }
        }

        private void InterestInputKeyPress(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if(InterestTextBox.Text != string.Empty) 
            { 
                if(e != null && e.Key == Key.Enter)
                {
                    AddInterest();
                }
            }
        }

        private void RemoveInterestButtonClick(object sender, RoutedEventArgs e)
        {
            int selectedIndex = InterestListBox.SelectedIndex;
            InterestListBox.Items.Remove(InterestListBox.Items[selectedIndex]);
        }

        private void RemoveInterestKeyPress(object sender, KeyEventArgs e)
        {
            int selectedIndex = InterestListBox.SelectedIndex;
            if (e != null && selectedIndex != -1 && e.Key == Key.Delete)
            {
                InterestListBox.Items.Remove(InterestListBox.Items[selectedIndex]);
            }
        }
    }
}
