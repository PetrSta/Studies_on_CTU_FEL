using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;

namespace SemesterWork___car_data_database.Support
{
    internal class ValidYearConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            // check if year is possible for our needs
            string valueString = (string)value;
            int year;
            int currentYear = DateTime.Now.Date.Year;
            bool result = false;

            if(Int32.TryParse(valueString, out year)) {
                if(year >= 1886 && year <= currentYear) { 
                    result = true;
                }
            }
            return result;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
