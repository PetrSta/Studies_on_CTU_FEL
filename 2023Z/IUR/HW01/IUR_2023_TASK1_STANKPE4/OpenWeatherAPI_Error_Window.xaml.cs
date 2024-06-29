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
    /// Interakční logika pro OpenWeatherAPI_Error_Window.xaml
    /// </summary>
    public partial class OpenWeatherAPI_Error_Window : Window
    {
        public OpenWeatherAPI_Error_Window()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            DialogResult = true;
        }
    }
}
