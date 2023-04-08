import React from "react";
import { act } from "react-dom/test-utils";
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter, Route } from "react-router-dom";
import { getLesson } from "../services/LessonService";
import { downloadFile } from "../services/FileService";
import ShowLesson from "./ShowLesson";

jest.mock("../services/LessonService");
jest.mock("../services/FileService");

const mockedGetLesson = getLesson;
const mockedDownloadFile = downloadFile;

describe("ShowLesson.test", () => {
    it("should render a lesson and download files when links are clicked", async () => {
        const mockLesson = {
            data: {
                metaInformation: {
                    name: "Test Lesson",
                    subject: "Test Subject",
                    grade: 9,
                    school: "Test School",
                    state: "Test State",
                    lessonThema: "Test Thema",
                    media: "Test Media",
                    lessonType: "Test Type",
                    learningGoals: "Test Goals",
                    preKnowledge: "Test Knowledge",
                    resources: "Test Resources",
                    keywords: "Test Keywords",
                    isPublic: false,
                },
                procedurePlan: [
                    {
                        name: "Test Phase",
                        minutes: 10,
                        learningGoal: "Test Learning Goal",
                        teacherAction: "Test Teacher Action",
                        pupilAction: "Test Pupil Action",
                        teachingForm: "Test Form",
                        media: "Test Media",
                        fileURIs: [{ uri: "http://example.com/test.pdf", mimeType: "application/pdf" }],
                    },
                ],
                lessonId: 1,
            },
        };
        mockedGetLesson.mockResolvedValueOnce(mockLesson);
        mockedDownloadFile.mockResolvedValueOnce({ data: new Uint8Array() });

        const mockLocation = {
            state: {
                id: 1,
            },
        };

        await act(async () => {
            render(
                <MemoryRouter initialEntries={[`/lesson/${mockLocation.state.id}`]}>
                    <Route path="/lesson/:id">
                        <ShowLesson />
                    </Route>
                </MemoryRouter>,
                { wrapper: ({ children }) => <div>{children}</div> }
            );
        });

        expect(screen.getByText(mockLesson.data.metaInformation.name)).toBeInTheDocument();

        const downloadButton = screen.getByText("Download");
        expect(downloadButton).toBeInTheDocument();

        await act(async () => {
            downloadButton.click();
            await waitFor(() => expect(mockedDownloadFile).toHaveBeenCalledTimes(1));
        });
    });
});