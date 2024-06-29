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

namespace SemesterWork___car_data_database.CustomItems
{
    /// <summary>
    /// Interakční logika pro FuelItem.xaml
    /// </summary>
    public partial class FuelItem : UserControl
    {
        public static readonly DependencyProperty FuelProperty = DependencyProperty.Register("Fuel", typeof(float?), typeof(FuelItem), new PropertyMetadata(0f));
        public static readonly DependencyProperty DateProperty = DependencyProperty.Register("Date", typeof(string), typeof(FuelItem));
        
        public float? Fuel
        {
            get => (float?)GetValue(FuelProperty);
            set => SetValue(FuelProperty, value);
        }
        
        public string Date
        {
            get => (string)GetValue(DateProperty);
            set => SetValue(DateProperty, value);
        }

        public static readonly RoutedEvent DoubleClickEvent = EventManager.RegisterRoutedEvent("DoubleClick", RoutingStrategy.Bubble, typeof(RoutedEventHandler), typeof(FuelItem));

        public event RoutedEventHandler DoubleClickHandler
        {
            add { AddHandler(DoubleClickEvent, value); }
            remove { RemoveHandler(DoubleClickEvent, value); }
        }
        private void DoubleCLick(object sender, MouseButtonEventArgs e)
        {
            if (e.ClickCount == 2)
            {
                RaiseEvent(new RoutedEventArgs(DoubleClickEvent));
            }
        }

        public FuelItem()
        {
            InitializeComponent();
        }
    }
}
