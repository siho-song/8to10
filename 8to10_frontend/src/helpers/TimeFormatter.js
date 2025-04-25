export function format24HourFormat(date, hour, minute) {
    const hourInt = parseInt(hour) % 12 + (hour >= 12 ? 12 : 0); // 24시간 형식으로 변환
    return `${date}T${hourInt.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00`;
}

export const formatDuration = (hour, minute) => {
    return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
};

export const formatPeriodTimeToLocalTimeFormat = (period, hour, minute) => {
    let hourInt = parseInt(hour);
    if (period === 'PM' && hourInt !== 12) {
        hourInt += 12;
    } else if (period === 'AM' && hour === 12) {
        hourInt = 0;
    }

    return `${hourInt.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
};

export function formatDateTime(dateTime) {
    const date = new Date(dateTime);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    let hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, '0');

    const ampm = hours >= 12 ? '오후' : '오전'; // AM/PM 결정
    hours = hours % 12; // 12시간제로 변환
    hours = hours ? String(hours).padStart(2, '0') : '12'; // 0일 경우 12로 표시

    return `${year}년 ${month}월 ${day}일 ${ampm} ${hours}시 ${minutes}분`;
}

export function formatDateTimeSimple(dateTime) {
    const date = new Date(dateTime);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    let hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, '0');

    const ampm = hours >= 12 ? '오후' : '오전'; // AM/PM 결정
    hours = hours % 12; // 12시간제로 변환
    hours = hours ? String(hours).padStart(2, '0') : '12'; // 0일 경우 12로 표시

    return `${year}.${month}.${day} ${ampm} ${hours}:${minutes}`;
}

export const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

export function formatLocalDateTimeToTime(dateString) {

    const [datePart, timePart] = dateString.split("T");
    const [hour, minute] = timePart.split(":").map(Number);

    const period = hour >= 12 ? "오후" : "오전";
    const formattedHour = hour % 12 || 12;

    const formattedMinute = String(minute).padStart(2, "0");

    return `${period} ${formattedHour}시 ${formattedMinute}분`;
}

export function formatBufferTime(bufferTime) {
    const [hour, minute, second] = bufferTime.split(":");
    return `${parseInt(hour)}시간 ${parseInt(minute)}분`;
}

export function  formatDateToLocalDateTime(date) {
    if (!(date instanceof Date)) {
        throw new Error("Invalid input: Expected a Date object");
    }

    // Pad single digit numbers with a leading zero
    const pad = (num) => String(num).padStart(2, "0");

    // Extract date and time components
    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1); // Months are zero-indexed
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());

    // Format as LocalDateTime (YYYY-MM-DDTHH:mm:ss)
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}


export function extractDateInfo(date) {
    if (!(date instanceof Date)) {
        throw new Error("Invalid input: Expected a Date object");
    }

    const pad = (num) => String(num).padStart(2, '0');
    const hour = date.getHours();
    const minute = date.getMinutes();

    return {
        date: `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`,
        period: hour < 12 ? 'AM' : 'PM',
        hour: hour % 12 || 12,
        minute: minute,
    };
}

export const formatDateInfo = ({ date, period, hour, minute }) => {
    const [year, month, day] = date.split('-').map(Number);
    const ampm = period === 'AM' ? '오전' : '오후';
    const hours = hour;
    const minutes = minute < 10 ? `0${minute}` : minute; // 분을 2자리 형식으로

    return `${year}년 ${month}월 ${day}일 ${ampm} ${hours}시 ${minutes}분`;
}


export function createLocalDateTime({ date, period, hour, minute }) {

    const [year, month, day] = date.split('-').map(Number);
    let normalizedHours = hour;

    if (period === 'PM' && hour !== 12) {
        normalizedHours = hour + 12;
    } else if (period === 'AM' && hour === 12) {
        normalizedHours = 0;
    }

    const pad = (num) => String(num).padStart(2, '0');
    return `${year}-${pad(month)}-${pad(day)}T${pad(normalizedHours)}:${pad(minute)}:00`;
}

export function dateToLocalDateTime(date) {
    if (!(date instanceof Date) || isNaN(date.getTime())) {
        throw new Error("Invalid Date object");
    }

    const pad = (num) => num.toString().padStart(2, "0");

    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1); // JavaScript의 month는 0부터 시작하므로 +1
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}
